package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.List;


/**
 * Created by stoycho.petrov on 17/02/2017.
 */

public class ContactsGridAdapter extends BaseAdapter{

    private List<UserModel> mContacts;
    private LayoutInflater  mLayoutInflater;
    private String[] mColors;

    public ContactsGridAdapter(Context context, List<UserModel> contacts)
    {
        mContacts              = contacts;
        mLayoutInflater        = ((Activity)context).getLayoutInflater();
        mColors                = context.getResources().getStringArray(R.array.favourite_colors);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int i) {
        return mContacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null){
            view          = mLayoutInflater.inflate(R.layout.item_grid_contact, null);

            ViewHolder viewHolder       = new ViewHolder();

            viewHolder.mNamePrefixTxt   = (TextView) view.findViewById(R.id.prefix_txt);
            viewHolder.mNameTxt         = (TextView) view.findViewById(R.id.name_txt);

            view.setTag(viewHolder);
        }

        ViewHolder  holder   = (ViewHolder) view.getTag();
        UserModel   contact  = mContacts.get(position);

        String namePrefix = contact.getFirstName().substring(0,1).toUpperCase();
        holder.mNamePrefixTxt.setText(namePrefix);
        holder.mNameTxt.setText(contact.getFirstName());

        view.setBackgroundColor(Color.parseColor(getColor(namePrefix)));
        return view;
    }

    public static class ViewHolder {

        private TextView mNamePrefixTxt;
        private TextView mNameTxt;
    }

    private String getColor(String letter)
    {
        int index = 0;
        for (char i = 'A'; i <= 'Z'; i ++)
        {
            if(letter.equalsIgnoreCase(String.valueOf(i)))
                return mColors[index];
            index++;
        }
        return null;
    }
}
