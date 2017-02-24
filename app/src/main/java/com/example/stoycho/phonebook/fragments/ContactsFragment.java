package com.example.stoycho.phonebook.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.Interfaces.OnClickViewInItem;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.RegistrationActivity;
import com.example.stoycho.phonebook.adapters.UsersRecyclerAdapter;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends BaseFragment implements View.OnClickListener,OnClickViewInItem,RadioGroup.OnCheckedChangeListener {

    private ListView                    mListView;
    private UsersRecyclerAdapter        mRecyclerAdapter;
    private List<CountryModel>          mCountries;
    private List<UserModel>             mUsers;
    private FloatingActionButton        mNewContactButton;
    private TextView                    mEmptyTxt;

    private String                      mFilterGender;
    private int                         mSelectedItemPosition;

    private final static int    PICK_IMAGE                              = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        initUI(root);
        
        mListView.setAdapter(mRecyclerAdapter);

        setListeners();
        loadUsers();

        return root;
    }

    private void initUI(View root)
    {
        mNewContactButton       = (FloatingActionButton)    root.findViewById(R.id.add_user);
        mListView               = (ListView)                root.findViewById(R.id.recycleView);
        mEmptyTxt               = (TextView)                root.findViewById(R.id.empty_txt);

        mUsers                  = new ArrayList<>();
        mRecyclerAdapter        = new UsersRecyclerAdapter(getActivity(),mUsers);
    }

    private void setListeners()
    {
        mNewContactButton.setOnClickListener(this);
        mRecyclerAdapter.setClickViewFromItem(this);
    }

    private void loadUsers()
    {
        mCountries      = new ArrayList<>();
        mUsers          = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null,null,true);  // make query for all users with their countries from Users table and Countries table

        mRecyclerAdapter.setUsers(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mEmptyTxt.setVisibility(View.GONE);
        }
        else {
            mEmptyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void refreshUsers(UserModel user, CountryModel country)
    {
        mCountries.add(country);
        mUsers.add(user);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
            mEmptyTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View clickedView) {

        switch (clickedView.getId())
        {
            case R.id.add_user:
                onAddUser();
                break;
        }
    }

    private void onAddUser()                                                           //start RegisterFragment
    {
        Intent registrationActivity = new Intent(getActivity(),RegistrationActivity.class);
        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        startActivity(registrationActivity);
    }

    private void updateUser(UserModel user, CountryModel country, int position)                     //update user in ListView
    {
        mUsers.set(position,user);
        mCountries.set(position,country);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    private void deleteUser(int position)                                                       //Delete user from database and listview
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(getActivity());

        if(usersDatabaseCommunication.deleteUserFromDatabase(mUsers.get(position))) {           //delete user from database, method return true if the query is successful
            mUsers.remove(position);
            mCountries.remove(position);
            mRecyclerAdapter.notifyDataSetChanged();

            if(mUsers.size() == 0) {
                mEmptyTxt.setVisibility(View.VISIBLE);
            }

            Toast.makeText(getActivity(), getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getActivity(),getString(R.string.notSuccessDelete),Toast.LENGTH_SHORT).show();
    }

    private void onEdit(int position)                                            //start RegistrationFragment for editing user information
    {
        UserModel user                      = mUsers.get(position);
        CountryModel country                = mCountries.get(position);

        Intent registrationActivity = new Intent(getActivity(),RegistrationActivity.class);
        registrationActivity.putExtra(Utils.BUNDLE_USER_KEY,user);
        registrationActivity.putExtra(Utils.BUNDLE_COUNTRY_KEY,country);
        registrationActivity.putExtra(Utils.BUNDLE_POSITION_KEY,position);
        startActivity(registrationActivity);
        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void selectImageForUser(int position)
    {
        mSelectedItemPosition = position;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
        RadioButton checkedButton = (RadioButton) radioGroup.findViewById(checkedButtonId);
        mFilterGender   = checkedButton.getText().toString();

        if(mFilterGender.equals(getString(R.string.all)))
            mFilterGender          = null;
    }

    public void refreshList(String searchString)
    {
        mUsers  = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null,searchString,true);
        mRecyclerAdapter.setUsers(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickView(View view, int position) {
        int clickedViewId = view.getId();
        switch (clickedViewId)
        {
            case R.id.call_button:
                callToNumber(mCountries.get(position).getCallingCode(),mUsers.get(position));
                break;
            case R.id.user_image:
                selectImageForUser(position);
                break;
        }
    }
}
