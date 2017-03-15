package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.HistoryAdapter;
import com.example.stoycho.phonebook.database.HistoryDatabaseComunication;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class HistoryFragment extends Fragment {

    private RecyclerView                mHistoryListView;
    private RecyclerView.LayoutManager  mLayoutManager;
    private HistoryAdapter              mHistoryAdapter;
    private List<HistoryModel>          mHistoryList;
    private HistoryDatabaseComunication mHistoryDatabaseComunication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        initUI(root);
        // Inflate the layout for this fragment
        return root;
    }

    private void initUI(View root) {

        mHistoryDatabaseComunication    = HistoryDatabaseComunication.getInstance(getActivity());
        mHistoryList                    = mHistoryDatabaseComunication.selectAllHistory(null);

        Collections.reverse(mHistoryList);

        try {
            setTypes(mHistoryList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mHistoryAdapter         = new HistoryAdapter(getActivity(),mHistoryList);
        mHistoryListView        = (RecyclerView) root.findViewById(R.id.history_list);
        mHistoryListView.setAdapter(mHistoryAdapter);

        mLayoutManager  = new LinearLayoutManager(getActivity());
        mHistoryListView.setLayoutManager(mLayoutManager);

    }

    private void setTypes(List<HistoryModel> histories) throws ParseException {

        Date      date    = null;
        Calendar    now     = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
        SimpleDateFormat format           = new SimpleDateFormat("dd MMMM yyyy");

        List<HistoryModel> newHistories = new ArrayList<>();

        for(int i = 0 ; i < histories.size(); i++)
        {
            HistoryModel historyModel = histories.get(i);
            Date historyDate = simpleDateFormat.parse(historyModel.getmDate());
            Date dateWithOutHours   = format.parse(format.format(historyDate));
            Date nowParsedDate      = format.parse(format.format(now.getTime()));
            if(date == null || dateWithOutHours.compareTo(date) != 0)
            {
                HistoryModel heather = new HistoryModel();
                if(dateWithOutHours.compareTo(nowParsedDate) == 0) {
                    heather.setmType(1);
                    heather.setmHeather("Today");
                }
                else if(getDifferenceInDays(nowParsedDate,dateWithOutHours) == 1){
                        heather.setmType(1);
                        heather.setmHeather("Yesterday");
                }
                else{
                    heather.setmType(1);
                    heather.setmHeather(format.format(historyDate));
                }
                newHistories.add(heather);

                date    =  dateWithOutHours;
            }
            historyModel.setmType(2);
            newHistories.add(historyModel);
        }

        mHistoryList = newHistories;
    }

    private long getDifferenceInDays(Date first, Date second){
        long diff = first.getTime() - second.getTime();

        return diff / (24 * 60 * 60 * 1000);
    }

    public void refreshList(String searchString) {

        mHistoryList    = mHistoryDatabaseComunication.selectAllHistory(searchString);
        mHistoryAdapter.setmHistoryList(mHistoryList);
        mHistoryAdapter.notifyDataSetChanged();
    }
}
