package com.example.stoycho.phonebook.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends FragmentActivity implements View.OnClickListener,View.OnTouchListener,TextWatcher,RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    private ViewPager           mViewPager;
    private ViewPagerAdapter    mViewPagerAdapter;
    private TabLayout           mTabLayout;

    private RelativeLayout      mBar;
    private ImageButton         mSearchButton;
    private ImageButton         mCloseButton;
    private TextView            mTitleTxt;
    private RelativeLayout      mFilterLayout;
    private int                 mFilterLayoutStartBottomMargin;

    private EditText            mCountryEdb;
    private EditText            mSearchCountryEdb;
    private RadioGroup          mGenderRadioGroup;
    private Button              mResetButton;
    private Button              mApplyButton;
    private CountryModel        mSelectedFilterCountry;

    private CountriesDatabaseCommunication  mCountriesDatabaseComunications;

    private int                 mFirstTouchPositionY;
    private boolean             mStartAnimation;
    private int                 mFilterCurrentTopMargin;
    private int                 mViewPagerPosition = 0;

    private final static String ALL_COUNTRIES_ARE_SELECTED              = "all_selected";


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

        hideFilterLayout();
    }

    private void initUI()
    {
        mViewPager          = (ViewPager)       findViewById(R.id.view_pager);
        mTabLayout          = (TabLayout)       findViewById(R.id.tabs_layout);
        mFilterLayout       = (RelativeLayout)  findViewById(R.id.filter_layout);
        mBar                = (RelativeLayout)  findViewById(R.id.bar);
        mTitleTxt           = (TextView)        findViewById(R.id.title);
        mSearchButton       = (ImageButton)     findViewById(R.id.search_button);
        mCloseButton        = (ImageButton)     findViewById(R.id.close_button);
        mCountryEdb         = (EditText)        findViewById(R.id.country_editbox);
        mSearchCountryEdb   = (EditText)        findViewById(R.id.search);
        mGenderRadioGroup   = (RadioGroup)      findViewById(R.id.gender_radio);
        mResetButton        = (Button)          findViewById(R.id.reset_button);
        mApplyButton        = (Button)          findViewById(R.id.apply_button);

        mCountriesDatabaseComunications =   CountriesDatabaseCommunication.getInstance(this);
    }

    private void setListeners()
    {
        mFilterLayout.setOnTouchListener(this);
        mSearchCountryEdb.addTextChangedListener(this);
        mSearchButton.setOnClickListener(this);
        mCountryEdb.setOnClickListener(this);
        mApplyButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mGenderRadioGroup.setOnCheckedChangeListener(this);
    }

    private void hideFilterLayout()
    {
        mFilterLayout.measure(0,0);
        mBar.measure(0,0);
        mTabLayout.measure(0,0);

        RelativeLayout.LayoutParams filterLayoutParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        mFilterLayoutStartBottomMargin         = filterLayoutParams.bottomMargin;
        filterLayoutParams.bottomMargin     = getResources().getDisplayMetrics().heightPixels;
        mFilterLayout.setLayoutParams(filterLayoutParams);
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

    private void animateFilter(float bottomMargin, float increaseHeight)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(bottomMargin,increaseHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).bottomMargin = ((Float) valueAnimator.getAnimatedValue()).intValue();
                mFilterLayout.requestLayout();
            }
        });

        valueAnimator.start();

//        if(increaseHeight == mFilterLayoutStartTopMargin)
//            mNewContactButton.setVisibility(View.GONE);
//        else
//            mNewContactButton.setVisibility(View.VISIBLE);
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
                mStartAnimation = false;

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

    private void applyFilter()
    {


    }

    private void resetFilter()
    {

    }

    private void setFilterCountry(CountryModel country)                                     //The contacts are selected by country
    {
        mSelectedFilterCountry  = country;

        if(country.getCountryName() == null) {
            mCountryEdb.setText(getString(R.string.all));
            mSelectedFilterCountry = null;
        }
        else
            mCountryEdb.setText(country.getCountryName());
    }

    private void onSelectCountry()                                                      //start CountriesFragment
    {
//        CountriesFragment countriesFragment = new CountriesFragment();
//        Bundle            bundle            = new Bundle();
//
//        bundle.putBoolean(ALL_COUNTRIES_ARE_SELECTED,true);
//        countriesFragment.setArguments(bundle);
//
//        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,0,0,R.anim.slide_up)
//                .add(R.id.replace_layout,countriesFragment, Utils.COIUNTRIES_FRAGMENT_TAG).addToBackStack(Utils.COUNTRY_BACKSTACK_NAME).commit();
    }

    @Override
    public void onClick(View view) {

        RelativeLayout.LayoutParams filterParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        switch (view.getId())
        {
            case R.id.close_button:
                replaceElementsWithFade(mCloseButton,mSearchButton);
                replaceElementsWithFade(mSearchCountryEdb, mTitleTxt);
                break;
            case R.id.reset_button:
                resetFilter();
                break;
            case R.id.apply_button:
                applyFilter();
                animateFilter(filterParams.bottomMargin,getResources().getDisplayMetrics().heightPixels - mBar.getHeight());
                break;
            case R.id.country_editbox:
                onSelectCountry();
                break;
            case R.id.search_button:
                replaceElementsWithFade(mTitleTxt, mSearchCountryEdb);
                replaceElementsWithFade(mSearchButton,mCloseButton);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int touchPositionY                    = (int) motionEvent.getRawY();

        RelativeLayout.LayoutParams layoutParams    = (RelativeLayout.LayoutParams) view
                .getLayoutParams();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                mFirstTouchPositionY = touchPositionY - lParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mFirstTouchPositionY - touchPositionY > 0) {
                    layoutParams.bottomMargin = mFirstTouchPositionY - touchPositionY;
                    layoutParams.topMargin = touchPositionY - mFirstTouchPositionY;
                    mFilterCurrentTopMargin = mFirstTouchPositionY - touchPositionY;
                    view.setLayoutParams(layoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mFilterCurrentTopMargin > mFilterLayout.getHeight() / 3)
                    animateFilter(mFilterCurrentTopMargin, mFilterLayout.getHeight());
                else {
                    animateFilter(mFilterCurrentTopMargin, mFilterLayoutStartBottomMargin);
                }
                break;
        }
        view.invalidate();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private void searchingByName(String charSequence,int fragmentIndex)
    {
        switch (fragmentIndex)
        {
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
}
