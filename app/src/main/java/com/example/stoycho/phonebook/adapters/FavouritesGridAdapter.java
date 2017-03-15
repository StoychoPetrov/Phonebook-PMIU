package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.List;


/**
 * Created by stoycho.petrov on 17/02/2017.
 */

public class FavouritesGridAdapter extends ArrayAdapter<UserModel>{

    private List<UserModel> mContacts;
    private LayoutInflater  mLayoutInflater;
    private Context         mContext;

    public FavouritesGridAdapter(Context context, List<UserModel> contacts) {
        super(context, R.layout.item_grid_contact, contacts);
        mContacts              = contacts;
        mContext               = context;
        mLayoutInflater        = ((Activity)context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public UserModel getItem(int i) {
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

        view.setBackgroundColor(Color.parseColor(Utils.getColor(mContext,namePrefix)));
        return view;
    }

    public static class ViewHolder {

        private TextView mNamePrefixTxt;
        private TextView mNameTxt;
    }

    public void setContactsList(List<UserModel> contacts)
    {
        mContacts   = contacts;
    }
}
