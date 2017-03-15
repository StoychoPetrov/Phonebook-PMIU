package com.example.stoycho.phonebook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by stoycho.petrov on 21/02/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryModel>                      mHistoryList;
    private UsersAndCountruesDatabaseComunication   mUsersAndCountruesDatabaseComunication;
    private Context                                 mContext;

    private static final int TYPE_SECTION_TITLE     = 1;
    private static final int TYPE_SECTION_HISTORY   = 2;

    public HistoryAdapter(Context context,List<HistoryModel> histories)
    {
        mContext        = context;
        mHistoryList    = histories;
        mUsersAndCountruesDatabaseComunication  = UsersAndCountruesDatabaseComunication.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == TYPE_SECTION_TITLE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_heather,parent,false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);


        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HistoryModel historyModel = mHistoryList.get(position);

        if(holder.mTypeId == TYPE_SECTION_TITLE)
            holder.mHeatherTxt.setText(historyModel.getmHeather());
        else{
            CountryModel countryModel = null;
            UserModel    userModel = null;

            if(historyModel.getmUserId() > -1)
                userModel   = mUsersAndCountruesDatabaseComunication.selectUserById(historyModel.getmUserId(),countryModel);

            SimpleDateFormat hourFormat         = new SimpleDateFormat("HH:mm");

            if(userModel != null)
                holder.mContactName.setText(userModel.getFirstName());
            else
                holder.mContactName.setText(historyModel.getmNotKnownPhone());

            try {
                holder.mDate.setText(hourFormat.format(new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy").parse(historyModel.getmDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            holder.mState.setText(CallingStatesDatabaseCommunication.getInstance(mContext).getNameOfState(historyModel.getmCallingStateId()));
        }
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView         mContactName;
        public TextView         mDate;
        public TextView         mHeatherTxt;
        public ImageView        mState;
        private int             mTypeId;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == TYPE_SECTION_TITLE) {
                mHeatherTxt = (TextView) itemView.findViewById(R.id.heather_txt);
                mTypeId     = TYPE_SECTION_TITLE;
            }
            else {
                mContactName    = (TextView) itemView.findViewById(R.id.contact_name);
                mDate           = (TextView) itemView.findViewById(R.id.date);
                mState          = (ImageView) itemView.findViewById(R.id.state);
                mTypeId         = TYPE_SECTION_HISTORY;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHistoryList.get(position).getmType() == TYPE_SECTION_TITLE) {
            return TYPE_SECTION_TITLE;
        }
        else
            return TYPE_SECTION_HISTORY;
    }


    public void setmHistoryList(List<HistoryModel> histories)
    {
        mHistoryList    =   histories;
    }
}
