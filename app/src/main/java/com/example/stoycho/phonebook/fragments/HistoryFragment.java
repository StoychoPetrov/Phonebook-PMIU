package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.HistoryAdapter;
import com.example.stoycho.phonebook.database.HistoryDatabaseComunication;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HistoryFragment extends Fragment {

    private ListView                    mHistoryListView;
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

    private void initUI(View root)
    {
        mHistoryDatabaseComunication = HistoryDatabaseComunication.getInstance(getActivity());
        mHistoryList            = mHistoryDatabaseComunication.selectAllHistory(null);
        Collections.reverse(mHistoryList);
        mHistoryAdapter         = new HistoryAdapter(getActivity(),R.layout.item_history,mHistoryList);

        mHistoryListView        = (ListView) root.findViewById(R.id.history_list);
        mHistoryListView.setAdapter(mHistoryAdapter);
    }

    public void refreshList(String searchString)
    {
        mHistoryList    = mHistoryDatabaseComunication.selectAllHistory(searchString);
        mHistoryAdapter.setmHistoryList(mHistoryList);
        mHistoryAdapter.notifyDataSetChanged();
    }
}
