package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.List;


/**
 * Created by stoycho.petrov on 17/02/2017.
 */

public class ContactsGridAdapter extends BaseAdapter{

    private Context         mContext;
    private List<UserModel> mContacts;
    private LayoutInflater  mLayoutInflater;

    public ContactsGridAdapter(Context context, List<UserModel> contacts)
    {
        mContext               = context;
        mContacts              = contacts;
        mLayoutInflater        = ((Activity)mContext).getLayoutInflater();
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
            view          = mLayoutInflater.inflate(R.layout.contact_grid_item, null);

            ViewHolder viewHolder       = new ViewHolder();

            viewHolder.mNamePrefixTxt   = (TextView) view.findViewById(R.id.prefix_txt);
            viewHolder.mNameTxt         = (TextView) view.findViewById(R.id.name_txt);

            view.setTag(viewHolder);
        }

        ViewHolder  holder   = (ViewHolder) view.getTag();
        UserModel   contact  = mContacts.get(position);

        holder.mNamePrefixTxt.setText(contact.getFirstName().substring(0,1));
        holder.mNameTxt.setText(contact.getFirstName());

        return view;
    }

    public static class ViewHolder {

        private TextView mNamePrefixTxt;
        private TextView mNameTxt;
    }
}
