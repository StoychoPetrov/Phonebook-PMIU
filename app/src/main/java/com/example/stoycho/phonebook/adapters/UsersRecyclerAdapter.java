package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnClickViewInItem;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.List;

/**
 * Created by stoycho.petrov on 09/12/2016.
 */

public class UsersRecyclerAdapter extends ArrayAdapter<UserModel> implements View.OnClickListener {

    private Context             mContext;
    private List<UserModel>     mUsers;
    private LayoutInflater      mLayoutInflater;
    private OnClickViewInItem   mClickListener;


    public UsersRecyclerAdapter(Context context, List<UserModel> users) {
        super(context, R.layout.item_user, users);

        mContext        = context;
        mUsers          = users;
        mLayoutInflater                             = ((Activity)context).getLayoutInflater();

    }

    public class ViewHolder {

        private TextView        mUserNameTxt;
        private ImageButton     mCallButton;
        private ImageView       mUserImage;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_user, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mUserNameTxt     = (TextView)        convertView.findViewById(R.id.user_name);
            viewHolder.mCallButton      = (ImageButton)     convertView.findViewById(R.id.call_button);
            viewHolder.mUserImage       = (ImageView)       convertView.findViewById(R.id.user_image);

            viewHolder.mCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickListener != null)
                        mClickListener.onClickView(viewHolder.mCallButton,position);
                }
            });

            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        UserModel user    = mUsers.get(position);
        holder.mUserNameTxt.setText(user.getFirstName());

        if(user.getmImage() != null)
            holder.mUserImage.setImageBitmap(BitmapFactory.decodeFile(user.getmImage()));
        else
            holder.mUserImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.ic_portrait_black_24dp));

        return convertView;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    public void setUsers(List<UserModel> users)
    {
        mUsers      = users;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.call_button:
                break;
        }
    }

    public void setClickViewFromItem(OnClickViewInItem listener)
    {
        mClickListener  = listener;
    }
}