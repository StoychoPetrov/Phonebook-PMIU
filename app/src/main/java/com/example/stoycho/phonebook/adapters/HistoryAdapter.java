package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by stoycho.petrov on 21/02/2017.
 */

public class HistoryAdapter extends ArrayAdapter<HistoryModel> {

    private List<HistoryModel>                      mHistoryList;
    private LayoutInflater                          mLayoutInflater;
    private UsersAndCountruesDatabaseComunication   mUsersAndCountruesDatabaseComunication;

    public HistoryAdapter(Context context, int resource, List<HistoryModel> historiesList) {
        super(context, resource, historiesList);

        mHistoryList                                = historiesList;
        mLayoutInflater                             = ((Activity)context).getLayoutInflater();
        mUsersAndCountruesDatabaseComunication      = UsersAndCountruesDatabaseComunication.getInstance(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView                                 = mLayoutInflater.inflate(R.layout.item_history, null);
            ViewHolder viewHolder                       = new ViewHolder();
            viewHolder.mContactName                     = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.mDate                            = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        }

        ViewHolder holder           = (ViewHolder) convertView.getTag();

        HistoryModel historyModel   = mHistoryList.get(position);

        CountryModel countryModel = null;
        UserModel    userModel;

        userModel   = mUsersAndCountruesDatabaseComunication.selectUserById(historyModel.getmUserId(),countryModel);

        holder.mContactName.setText(userModel.getFirstName());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat   = new SimpleDateFormat("E MMM dd HH:MM");
        SimpleDateFormat hourFormat         = new SimpleDateFormat("HH:MM");

        try {
            Date date    = simpleDateFormat.parse(historyModel.getmDate());
            Calendar phoneCallDate = Calendar.getInstance();
            phoneCallDate.setTime(date);

            if(phoneCallDate.compareTo(calendar) == 0)
                holder.mDate.setText(hourFormat.format(date));
            else if(phoneCallDate.before(calendar))
                holder.mDate.setText(hourFormat.format(date));
            else
                holder.mDate.setText("Tomorrow");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public int getCount()
    {
        return mHistoryList.size();
    }

    private static class ViewHolder {
        public TextView        mContactName;
        public TextView        mDate;
    }

    public void setmHistoryList(List<HistoryModel> histories)
    {
        mHistoryList    =   histories;
    }
}
