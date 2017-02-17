package com.example.stoycho.phonebook.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.ContactsGridAdapter;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private GridView        mGridView;
    private List<UserModel> mContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_favourites, container, false);
        initUI(root);

        UsersAndCountruesDatabaseComunication usersAndCountruesDatabaseComunication  = UsersAndCountruesDatabaseComunication.getInstance(getActivity());
        mContacts  = usersAndCountruesDatabaseComunication.selectUsersAndTheirCountries(new ArrayList<CountryModel>(), Utils.INVALID_ROW_INDEX,null,null,null);

        ContactsGridAdapter gridAdapter = new ContactsGridAdapter(getActivity(),mContacts);
        mGridView.setAdapter(gridAdapter);
        // Inflate the layout for this fragment
        return root;
    }

    private void initUI(View root)
    {
        mGridView   = (GridView) root.findViewById(R.id.grid_view);
    }
}
