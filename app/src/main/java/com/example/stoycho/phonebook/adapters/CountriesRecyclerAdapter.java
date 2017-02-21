package com.example.stoycho.phonebook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.models.CountryModel;

import java.util.List;

/**
 * Created by Stoycho on 10/21/2016.
 */

public class CountriesRecyclerAdapter extends RecyclerView.Adapter<CountriesRecyclerAdapter.ViewHolder> {

    private Context             mContext;
    private List<CountryModel>       mCountries;
    private OnRecyclerItemClick mOnItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCountryNameTxt;
        private TextView mCallingCodeTxt;

        private ViewHolder(View itemView) {
            super(itemView);
            mCountryNameTxt = (TextView) itemView.findViewById(R.id.countryName);
            mCallingCodeTxt = (TextView) itemView.findViewById(R.id.callingCode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener != null)
                mOnItemClickListener.onRecyclerItemClickListener(view,getAdapterPosition());
        }
    }

    public CountriesRecyclerAdapter(Context context,List<CountryModel> countries)
    {
        mContext    = context;
        mCountries  = countries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCountryNameTxt.setText(mCountries.get(position).getCountryName());
        String callingNumber = mCountries.get(position).getCallingCode();

        if(callingNumber != null && !callingNumber.equals("")) {
            String callingCode = mContext.getString(R.string.left_scope) + callingNumber + mContext.getString(R.string.right_scope);
            holder.mCallingCodeTxt.setText(callingCode);
        }
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    public void setCountries(List<CountryModel> countries)
    {
        mCountries = countries;
    }

    public void setOnItemClickListener(OnRecyclerItemClick onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }
}
