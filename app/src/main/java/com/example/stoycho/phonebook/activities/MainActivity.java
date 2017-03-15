package com.example.stoycho.phonebook.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.ViewPagerAdapter;
import com.example.stoycho.phonebook.communicationClasses.HttpRequest;
import com.example.stoycho.phonebook.database.CallingStatesDatabaseCommunication;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends FragmentActivity implements View.OnClickListener,TextWatcher,RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    private ViewPager           mViewPager;
    private ViewPagerAdapter    mViewPagerAdapter;
    private TabLayout           mTabLayout;

    private RelativeLayout      mBar;
    private ImageButton         mSearchButton;
    private ImageButton         mCloseButton;
    private TextView            mTitleTxt;

    private EditText            mSearchCountryEdb;
    private RadioGroup          mGenderRadioGroup;

    private CountriesDatabaseCommunication  mCountriesDatabaseComunications;

    private int                 mFirstTouchPositionY;
    private int                 mFilterCurrentTopMargin;
    private int                 mViewPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setListeners();

        try {
            if(mCountriesDatabaseComunications.getCountOfCountries() == 0)
                loadCountries();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        insertCallingStatesInDatabase();
    }

    private void initUI()
    {
        mViewPager          = (ViewPager)       findViewById(R.id.view_pager);
        mTabLayout          = (TabLayout)       findViewById(R.id.tabs_layout);
        mBar                = (RelativeLayout)  findViewById(R.id.bar);
        mTitleTxt           = (TextView)        findViewById(R.id.title);
        mSearchButton       = (ImageButton)     findViewById(R.id.search_button);
        mCloseButton        = (ImageButton)     findViewById(R.id.close_button);
        mSearchCountryEdb   = (EditText)        findViewById(R.id.search);
        mGenderRadioGroup   = (RadioGroup)      findViewById(R.id.gender_radio);

        mCountriesDatabaseComunications =   CountriesDatabaseCommunication.getInstance(this);
    }

    private void setListeners()
    {
        mSearchCountryEdb.addTextChangedListener(this);
        mSearchButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    private void insertCallingStatesInDatabase() {

        CallingStatesDatabaseCommunication callingStatesDatabaseCommunication = CallingStatesDatabaseCommunication.getInstance(this);

        if(callingStatesDatabaseCommunication.getCountOfStates() == 0) {

            String[] states = getResources().getStringArray(R.array.calling_states);

            for(String state : states){
                callingStatesDatabaseCommunication.insertCallingState(state);
            }
        }
    }

    private void loadCountries() throws MalformedURLException {
        CountriesDatabaseCommunication countryDatabaseCommunication   = CountriesDatabaseCommunication.getInstance(this);

        if(countryDatabaseCommunication.getCountOfCountries() == 0){
            HttpRequest httpRequest = new HttpRequest()
            {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(s != null)
                    {
                        try {

                            JSONArray countries = new JSONArray(s);
                            deserializeCountris(countries);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            httpRequest.execute(new URL(Utils.URL_FOR_COUNTRIES));
        }
    }

    private void deserializeCountris(JSONArray countriesJsonArray) throws JSONException {

        for (int i = 0; i < countriesJsonArray.length(); i++)
        {
            CountryModel countryModel = new CountryModel();
            JSONObject countryJson  = countriesJsonArray.getJSONObject(i);

            if(countryJson.has(Utils.JSON_COUNTRY_NAME_KEY))
                countryModel.setCountryName(countryJson.getString(Utils.JSON_COUNTRY_NAME_KEY));

            if(countryJson.has(Utils.JSON_COUNTRY_CALLING_CODE_KEY) && countryJson.getJSONArray(Utils.JSON_COUNTRY_CALLING_CODE_KEY).length() > 0)
                countryModel.setCallingCode(countryJson.getJSONArray(Utils.JSON_COUNTRY_CALLING_CODE_KEY).getString(0));

            mCountriesDatabaseComunications.saveInDatabase(countryModel);
        }
    }

    private void replaceElementsWithFade(final View visibleView, final View goneView)
    {

        AlphaAnimation fadeInAnimation      = new AlphaAnimation(0,1);
        AlphaAnimation fadeOutAnimation     = new AlphaAnimation(1,0);

        fadeOutAnimation.setDuration(getResources().getInteger(R.integer.fade_animation_duration));
        fadeOutAnimation.setInterpolator(new LinearInterpolator());

        fadeInAnimation.setDuration(getResources().getInteger(R.integer.fade_animation_duration));
        fadeInAnimation.setInterpolator(new LinearInterpolator());
        fadeInAnimation.setStartOffset(getResources().getInteger(R.integer.fade_animation_start_offset));

        visibleView.startAnimation(fadeOutAnimation);
        goneView.startAnimation(fadeInAnimation);

        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goneView.setVisibility(View.VISIBLE);

                if(goneView.getId() == R.id.search) {
                    mSearchCountryEdb.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mSearchCountryEdb, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(visibleView.getId() == R.id.search)
                {
                    mSearchCountryEdb.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchCountryEdb.getWindowToken(), 0);
                }

                visibleView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.close_button:
                replaceElementsWithFade(mCloseButton,mSearchButton);
                replaceElementsWithFade(mSearchCountryEdb, mTitleTxt);
                break;
            case R.id.search_button:
                replaceElementsWithFade(mTitleTxt, mSearchCountryEdb);
                replaceElementsWithFade(mSearchButton,mCloseButton);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private void searchingByName(String charSequence,int fragmentIndex)
    {
        if(mViewPager != null) {
            switch (fragmentIndex) {
                case 0:
                    mViewPagerAdapter.getmFavouritesFragment().refreshList(charSequence);
                    break;
                case 1:
                    mViewPagerAdapter.getmHistoryFragment().refreshList(charSequence);
                    break;
                case 2:
                    mViewPagerAdapter.getmContactsFragment().refreshList(charSequence);
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       searchingByName(charSequence.toString(), mViewPager.getCurrentItem());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(mSearchCountryEdb.getVisibility() == View.VISIBLE) {
            replaceElementsWithFade(mCloseButton, mSearchButton);
            replaceElementsWithFade(mSearchCountryEdb, mTitleTxt);
            if(!mSearchCountryEdb.getText().toString().equalsIgnoreCase("")) {
                searchingByName("",mViewPagerPosition);
                mSearchCountryEdb.setText("");
            }
        }
        mViewPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == Utils.ADD_CONTACT_REQUEST_CODE  || requestCode == Utils.EDIT_CONTACT_REQUEST_CODE) {
                mViewPagerAdapter.getmContactsFragment().loadUsers();
                mViewPagerAdapter.getmHistoryFragment().refreshList("");
            }
        }
    }
}
