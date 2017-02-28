package com.example.stoycho.phonebook.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.MainActivity;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.database.HistoryDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by stoycho.petrov on 24/02/2017.
 */

public class PhoneCallReceiver extends CallingReceiver {
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {

      saveInHistories(ctx,number,Utils.STATE_INCOMMING,start);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
      saveInHistories(ctx,number,Utils.STATE_OUTGOING,start);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        saveInHistories(ctx,number,Utils.STATE_MISSED,start);
    }

    private String getCountryCode(Context context,String phone){

        CountriesDatabaseCommunication countriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(context);
        List<CountryModel>             countries                      = countriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);

        for(CountryModel countryModel : countries) {
            if (countryModel.getCallingCode() != null && !countryModel.getCallingCode().equalsIgnoreCase("")) {
                if (phone.contains("+" + countryModel.getCallingCode()))
                    return countryModel.getCallingCode();
            }
        }
        return null;
    }

    private void saveInHistories(Context context, String number, int state, Date date){
        if(number != null && !number.equalsIgnoreCase("")) {
            String      countryCode = getCountryCode(context, number);
            UserModel   userModel = null;
            int callingCount = -1;
            if (countryCode != null)
                userModel = UsersDatabaseCommunication.getInstance(context).getUserIdByPhone(number.substring(countryCode.length() + 1),callingCount);

            if (userModel != null && userModel.getId() >= 0) {
                HistoryModel historyModel = new HistoryModel(date.toString(), userModel.getId());
                historyModel.setmCallingStateId(state);
                HistoryDatabaseComunication.getInstance(context).insertIntoHistoryTable(historyModel);
                UsersDatabaseCommunication.getInstance(context).updateCallsCounts(userModel.getId(),userModel.getmCallsCount() + 1);
                createNotification(context,number,userModel);
            }
            else {
                HistoryModel historyModel = new HistoryModel();
                historyModel.setmDate(date.toString());
                historyModel.setmNotKnownPhone(number);

                HistoryDatabaseComunication.getInstance(context).insertIntoHistoryTable(historyModel);
                createNotification(context,number,null);
            }
        }
    }

    private void createNotification(Context context,String number,UserModel userModel) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_phone_missed_white_24dp);
        builder.setContentTitle("You missed call");

        if(userModel != null && userModel.getFirstName() != null && userModel.getFirstName().equalsIgnoreCase(""))
            builder.setContentText(userModel.getFirstName());
        else if(number != null && number.equalsIgnoreCase(""))
            builder.setContentText(number);

        Intent              resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder    stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setAutoCancel(true);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(-1, builder.build());

    }
}