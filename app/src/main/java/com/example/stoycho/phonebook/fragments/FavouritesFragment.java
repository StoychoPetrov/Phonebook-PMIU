package com.example.stoycho.phonebook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.FavouritesGridAdapter;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends BaseFragment implements GridView.OnItemClickListener {

    private GridView                mGridView;
    private FavouritesGridAdapter   mGridAdapter;
    private List<UserModel>         mContacts;
    private List<CountryModel>      mCountries;
    private TextView                mEmptyTxt;

    private UsersAndCountruesDatabaseComunication usersAndCountruesDatabaseComunication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_favourites, container, false);
        initUI(root);
        setListeners();

        usersAndCountruesDatabaseComunication   = UsersAndCountruesDatabaseComunication.getInstance(getActivity());
        mContacts                               = usersAndCountruesDatabaseComunication.selectFavurites(mCountries);

        mGridAdapter = new FavouritesGridAdapter(getActivity(),mContacts);
        mGridView.setAdapter(mGridAdapter);

        if(mContacts.size() < 1)
            mEmptyTxt.setVisibility(View.VISIBLE);
        else
            mEmptyTxt.setVisibility(View.GONE);

        return root;
    }

    private void initUI(View root)
    {
        mGridView   = (GridView) root.findViewById(R.id.grid_view);
        mEmptyTxt   = (TextView) root.findViewById(R.id.empty_txt);
        mCountries  = new ArrayList<>();
    }

    private void setListeners()
    {
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        callToNumber(mCountries.get(position).getCallingCode(),mContacts.get(position));
    }

    public void refreshList(String searchStirng)
    {
        mContacts   = usersAndCountruesDatabaseComunication.selectUsersAndTheirCountries(mCountries,Utils.INVALID_ROW_INDEX,null,null,searchStirng,false);
        mGridAdapter.setContactsList(mContacts);
        mGridAdapter.notifyDataSetChanged();

        if(mContacts.size() < 1)
            mEmptyTxt.setVisibility(View.VISIBLE);
        else
            mEmptyTxt.setVisibility(View.GONE);
    }
}
