package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.RegistrationActivity;
import com.example.stoycho.phonebook.adapters.CountriesRecyclerAdapter;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CountriesFragment extends Fragment implements View.OnClickListener,OnRecyclerItemClick {

    private RecyclerView                    mCountriesRecycler;
    private CountriesRecyclerAdapter        mCountriesAdapter;
    private List<CountryModel>                   mCountries;
    private TextView                        mSearchTxt;
    private View                            mDividerView;
    private CountriesDatabaseCommunication  mCountriesDatabaseCommunication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_countries, container, false);

        initUI(root);
        setListeners();

        mCountriesRecycler.setHasFixedSize(true);
        mCountriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCountriesRecycler.setItemAnimator(new DefaultItemAnimator());
        mCountriesRecycler.setAdapter(mCountriesAdapter);

        if(getArguments() == null) {
            mSearchTxt.setVisibility(View.GONE);
            mDividerView.setVisibility(View.GONE);
        }
        loadCountriesList();
        return root;
    }

    private void initUI(View root)
    {
        mCountriesRecycler              = (RecyclerView)    root.findViewById(R.id.countriesRecycler);
        mSearchTxt                      = (TextView)        root.findViewById(R.id.all);
        mDividerView                    =                   root.findViewById(R.id.divider);

        mCountriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(getActivity());
        mCountries                      = new ArrayList<>();
        mCountriesAdapter               = new CountriesRecyclerAdapter(getActivity(),mCountries);
    }

    private void setListeners()
    {
        mSearchTxt.setOnClickListener(this);
        mCountriesAdapter.setOnItemClickListener(this);
    }

    private void loadCountriesList()
    {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();
        switch (id)
        {
            case R.id.all:
                selectAll();
                break;
        }
    }

    public void searchCountries(String input)
    {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_SEARCH_PLACES,input);
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    private void selectAll()
    {
        getActivity().getIntent().putExtra(Utils.INTENT_FILTER_COUNTRY_KEY, new CountryModel());
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRecyclerItemClickListener(View view, int position) {

        if(getActivity() instanceof RegistrationActivity) {
            ((RegistrationActivity)getActivity()).setSelectedCountry(mCountries.get(position));
        }
        else{
            getActivity().getIntent().putExtra(Utils.INTENT_FILTER_COUNTRY_KEY, mCountries.get(position));                                                          // Put extra in activity intent.
            getFragmentManager().popBackStack();                                                                                                                                                            // pop all fragments
        }

        View viewFocus = getActivity().getCurrentFocus();

        if (viewFocus != null) {                                                                                                                                         // check if any edittext is focused and close keyboard if it is opened.
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
        getFragmentManager().popBackStack();
    }
}
