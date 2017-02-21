package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.HistoryDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

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

        return convertView;
    }

    private static class ViewHolder {
        public TextView        mContactName;
        public TextView        mDate;
    }
}
