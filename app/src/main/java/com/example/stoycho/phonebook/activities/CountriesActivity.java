package com.example.stoycho.phonebook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.CountriesAdapter;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CountriesActivity extends BaseActivity implements AdapterView.OnItemClickListener,TextWatcher {

    private ListView                        mCountriesRecycler;
    private EditText                        mSearchEdb;
    private CountriesAdapter mCountriesAdapter;
    private List<CountryModel>              mCountries;
    private CountriesDatabaseCommunication  mCountriesDatabaseCommunication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        
        initUI();
        setListeners();

        mCountriesRecycler.setAdapter(mCountriesAdapter);
        
        loadCountriesList();
    }

    private void initUI()
    {
        mCountriesRecycler              = (ListView)    findViewById(R.id.countriesRecycler);
        mSearchEdb                      = (EditText)        findViewById(R.id.search);
        mCountriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(this);
        mCountries                      = new ArrayList<>();
        mCountriesAdapter               = new CountriesAdapter(this,mCountries);
    }

    private void setListeners()
    {
        mCountriesRecycler.setOnItemClickListener(this);
        mSearchEdb.addTextChangedListener(this);
    }

    private void loadCountriesList()
    {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    public void searchCountries(String input)
    {
        mCountries = mCountriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_SEARCH_PLACES,input);
        mCountriesAdapter.setCountries(mCountries);
        mCountriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        searchCountries(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent = new Intent();
        intent.putExtra(Utils.BUNDLE_COUNTRY_KEY,mCountries.get(position));
        setResult(RESULT_OK,intent);
        finish();

        View viewFocus = getCurrentFocus();

        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }

    }
}
