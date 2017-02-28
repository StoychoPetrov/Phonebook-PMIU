package com.example.stoycho.phonebook.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
import com.example.stoycho.phonebook.utils.Utils;

import java.util.List;

/**
 * Created by stoycho.petrov on 09/12/2016.
 */

public class UsersAdapter extends ArrayAdapter<UserModel> {

    private Context             mContext;
    private List<UserModel>     mUsers;
    private LayoutInflater      mLayoutInflater;
    private OnClickViewInItem   mClickListener;


    public UsersAdapter(Context context, List<UserModel> users) {
        super(context, R.layout.item_user, users);

        mContext        = context;
        mUsers          = users;
        mLayoutInflater                             = ((Activity)context).getLayoutInflater();

    }

    public class ViewHolder {

        private TextView        mUserNameTxt;
        private ImageView     mCallButton;
        private TextView        mUserImage;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_user, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.mUserNameTxt     = (TextView)        convertView.findViewById(R.id.user_name);
            viewHolder.mCallButton      = (ImageView)     convertView.findViewById(R.id.call_button);
            viewHolder.mUserImage       = (TextView)        convertView.findViewById(R.id.user_image);

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

        holder.mUserImage.setText(user.getFirstName().substring(0,1).toUpperCase());

        Drawable background = holder.mUserImage.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Utils.getColor(mContext,user.getFirstName().substring(0,1))));
            holder.mUserImage.setBackground(background);
        }

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

    public void setClickViewFromItem(OnClickViewInItem listener)
    {
        mClickListener  = listener;
    }
}