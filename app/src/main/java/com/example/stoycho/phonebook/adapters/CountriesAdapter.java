package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.CountryModel;

import java.util.List;

/**
 * Created by Stoycho on 10/21/2016.
 */

public class CountriesAdapter extends ArrayAdapter<CountryModel> {

    private Context             mContext;
    private List<CountryModel>       mCountries;
    private LayoutInflater                          mLayoutInflater;

    public CountriesAdapter(Context context, List<CountryModel> countries) {
        super(context, R.layout.item_country, countries);

        mLayoutInflater     = ((Activity)context).getLayoutInflater();
        mContext            = context;
        mCountries          = countries;
    }

    private class ViewHolder{

        private TextView mCountryNameTxt;
        private TextView mCallingCodeTxt;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView                                 =  mLayoutInflater.inflate(R.layout.item_country, null);
            ViewHolder viewHolder                       = new ViewHolder();

            viewHolder.mCallingCodeTxt                  = (TextView) convertView.findViewById(R.id.callingCode);
            viewHolder.mCountryNameTxt                  = (TextView) convertView.findViewById(R.id.countryName);

            convertView.setTag(viewHolder);
        }

        ViewHolder holder           = (ViewHolder) convertView.getTag();

        holder.mCountryNameTxt.setText(mCountries.get(position).getCountryName());
        String callingNumber = mCountries.get(position).getCallingCode();

        if(callingNumber != null && !callingNumber.equals("")) {
            String callingCode = mContext.getString(R.string.left_scope) + callingNumber + mContext.getString(R.string.right_scope);
            holder.mCallingCodeTxt.setText(callingCode);
        }

        return convertView;
    }

    @Override
    public int getCount()
    {
        return mCountries.size();
    }

    public void setCountries(List<CountryModel> countries)
    {
        mCountries = countries;
    }
}
