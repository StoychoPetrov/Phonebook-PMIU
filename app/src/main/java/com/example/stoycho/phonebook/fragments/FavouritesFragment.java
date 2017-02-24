package com.example.stoycho.phonebook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.ContactsGridAdapter;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends BaseFragment implements GridView.OnItemClickListener {

    private GridView            mGridView;
    private ContactsGridAdapter mGridAdapter;
    private List<UserModel>     mContacts;
    private List<CountryModel>  mCountries;

    private UsersAndCountruesDatabaseComunication usersAndCountruesDatabaseComunication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_favourites, container, false);
        initUI(root);
        setListeners();

        usersAndCountruesDatabaseComunication  = UsersAndCountruesDatabaseComunication.getInstance(getActivity());
        usersAndCountruesDatabaseComunication.setOrderByCallingCount(true);
        mContacts  = usersAndCountruesDatabaseComunication.selectUsersAndTheirCountries(mCountries, Utils.INVALID_ROW_INDEX,null,null,null,false);
        usersAndCountruesDatabaseComunication.setOrderByCallingCount(false);

        mGridAdapter = new ContactsGridAdapter(getActivity(),mContacts);
        mGridView.setAdapter(mGridAdapter);

        return root;
    }

    private void initUI(View root)
    {
        mGridView   = (GridView) root.findViewById(R.id.grid_view);
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
        usersAndCountruesDatabaseComunication.setOrderByCallingCount(true);
        mContacts   = usersAndCountruesDatabaseComunication.selectUsersAndTheirCountries(mCountries,Utils.INVALID_ROW_INDEX,null,null,searchStirng,false);
        mGridAdapter.setContactsList(mContacts);
        mGridAdapter.notifyDataSetChanged();
    }
}
