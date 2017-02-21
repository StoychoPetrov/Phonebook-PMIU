package com.example.stoycho.phonebook.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.Interfaces.OnRecyclerItemClick;
import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.activities.RegistrationActivity;
import com.example.stoycho.phonebook.adapters.UsersRecyclerAdapter;
import com.example.stoycho.phonebook.communicationClasses.HttpRequest;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends BaseFragment implements View.OnClickListener,FragmentManager.OnBackStackChangedListener,OnRecyclerItemClick,RadioGroup.OnCheckedChangeListener,View.OnTouchListener,TextWatcher {

    private RecyclerView                mRecyclerView;
    private UsersRecyclerAdapter        mRecyclerAdapter;
    private List<CountryModel>          mCountries;
    private List<UserModel>             mUsers;
    private FloatingActionButton        mNewContactButton;
//    private TextView                    mTitleTxt;
    private TextView                    mEmptyTxt;
    private EditText                    mCountryEdb;
    private EditText                    mSearchCountryEdb;
    private CountryModel                mSelectedFilterCountry;
    private RelativeLayout              mFilterLayout;
//    private RelativeLayout              mBar;
//    private ImageButton                 mFilterButton;
//    private ImageButton                 mSearchButton;
//    private ImageButton                 mCloseButton;
    private RadioGroup                  mGenderRadioGroup;
    private Button                      mResetButton;
    private Button                      mApplyButton;

    private String                      mFilterGender;
    private int                         mFirstTouchPositionY;
    private int                         mFilterCurrentTopMargin;
    private int                         mFilterLayoutStartTopMargin;
    private boolean                     mStartAnimation;
    private boolean                     mNotCountrySearching;
    private int                         mSelectedItemPosition;

    private CountriesDatabaseCommunication  mCountriesDatabaseComunications;

    private final static int    PICK_IMAGE                              = 1;
    private final static String ALL_COUNTRIES_ARE_SELECTED              = "all_selected";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(root);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);

        setListeners();
        loadUsers();
        try {
            if(mCountriesDatabaseComunications.getCountOfCountries() == 0)
                loadCountries();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        mFilterLayout.measure(0,0);
//        mBar.measure(0,0);
        RelativeLayout.LayoutParams filterLayoutParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        mFilterLayoutStartTopMargin         = filterLayoutParams.bottomMargin;
        filterLayoutParams.bottomMargin     = (int) (getResources().getDisplayMetrics().heightPixels - 50 * getResources().getDisplayMetrics().density);
        filterLayoutParams.topMargin        = (int) (50 * getResources().getDisplayMetrics().density - getResources().getDisplayMetrics().heightPixels);
        mFilterLayout.setLayoutParams(filterLayoutParams);


        return root;
    }

    private void initUI(View root)
    {
        mNewContactButton       = (FloatingActionButton)    root.findViewById(R.id.add_user);
        mRecyclerView           = (RecyclerView)            root.findViewById(R.id.recycleView);
        mFilterLayout           = (RelativeLayout)          root.findViewById(R.id.filter_layout);
//        mBar                    = (RelativeLayout)          root.findViewById(R.id.bar);
//        mTitleTxt               = (TextView)                root.findViewById(R.id.title);
        mEmptyTxt               = (TextView)                root.findViewById(R.id.empty_txt);
//        mFilterButton           = (ImageButton)             root.findViewById(R.id.filter_button);
//        mSearchButton           = (ImageButton)             root.findViewById(R.id.search_button);
//        mCloseButton            = (ImageButton)             root.findViewById(R.id.close_button);
        mCountryEdb             = (EditText)                root.findViewById(R.id.country_editbox);
        mSearchCountryEdb       = (EditText)                root.findViewById(R.id.search);
        mGenderRadioGroup       = (RadioGroup)              root.findViewById(R.id.gender_radio);
        mResetButton            = (Button)                  root.findViewById(R.id.reset_button);
        mApplyButton            = (Button)                  root.findViewById(R.id.apply_button);

        mRecyclerAdapter        = new UsersRecyclerAdapter(getActivity(),mUsers);

        mCountriesDatabaseComunications =   CountriesDatabaseCommunication.getInstance(getActivity());
    }

    private void setListeners()
    {
        mNewContactButton.setOnClickListener(this);
        mRecyclerAdapter.setOnItemClickListener(this);
//        mFilterButton.setOnClickListener(this);
        mCountryEdb.setOnClickListener(this);
        mGenderRadioGroup.setOnCheckedChangeListener(this);
        mFilterLayout.setOnTouchListener(this);
//        mSearchCountryEdb.addTextChangedListener(this);
//        mSearchButton.setOnClickListener(this);
        mApplyButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
//        mCloseButton.setOnClickListener(this);
    }

    private void animateFilter(float topMargin, float increaseHeight)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(topMargin,increaseHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).bottomMargin = ((Float) valueAnimator.getAnimatedValue()).intValue();
                ((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).topMargin = -((Float) valueAnimator.getAnimatedValue()).intValue();
                mFilterLayout.requestLayout();
            }
        });

        valueAnimator.start();

        if(increaseHeight == mFilterLayoutStartTopMargin)
            mNewContactButton.setVisibility(View.GONE);
        else
            mNewContactButton.setVisibility(View.VISIBLE);
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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchCountryEdb.getWindowToken(), 0);
                }

                visibleView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadUsers()
    {
        mCountries      = new ArrayList<>();
        mUsers          = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(mCountries,UsersAndCountruesDatabaseComunication.WITHOUT_COUNTRY_ID,null,null,null,true);  // make query for all users with their countries from Users table and Countries table

        mRecyclerAdapter.setUsersAndCountries(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
//            mFilterButton.setVisibility(View.VISIBLE);
//            mSearchButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
        else {
//            mFilterButton.setVisibility(View.GONE);
//            mSearchButton.setVisibility(View.GONE);
            mEmptyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void refreshUsers(UserModel user, CountryModel country)
    {
        mCountries.add(country);
        mUsers.add(user);
        mRecyclerAdapter.notifyDataSetChanged();

        if(mUsers.size() > 0) {
//            mSearchButton.setVisibility(View.VISIBLE);
//            mFilterButton.setVisibility(View.VISIBLE);
            mEmptyTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();
        RelativeLayout.LayoutParams filterParams = (RelativeLayout.LayoutParams) mFilterLayout.getLayoutParams();

        switch (id)
        {
            case R.id.add_user:
                onAddUser();
//                mTitleTxt.setText(getString(R.string.registration));
                break;
            case R.id.filter_button:
                if(filterParams.bottomMargin > mFilterLayoutStartTopMargin)
                    animateFilter(filterParams.bottomMargin,mFilterLayoutStartTopMargin);
                else
//                    animateFilter(mFilterLayoutStartTopMargin,getResources().getDisplayMetrics().heightPixels - mBar.getHeight());
                break;
            case R.id.country_editbox:
                onSelectCountry();
                break;
            case R.id.search_button:
//                replaceElementsWithFade(mTitleTxt, mSearchCountryEdb);
//                replaceElementsWithFade(mSearchButton,mCloseButton);
                break;
            case R.id.reset_button:
                resetFilter();
                break;
            case R.id.apply_button:
                applyFilter();
//                animateFilter(filterParams.bottomMargin,getResources().getDisplayMetrics().heightPixels - mBar.getHeight());
                break;
            case R.id.close_button:
//                replaceElementsWithFade(mCloseButton,mSearchButton);
//                replaceElementsWithFade(mSearchCountryEdb, mTitleTxt);
                break;
        }
    }

    private void applyFilter()
    {
        int filterCountryId;
        int checkedButtonId     = mGenderRadioGroup.getCheckedRadioButtonId();

        String gender           = ((RadioButton)    mGenderRadioGroup.findViewById(checkedButtonId)).getText().toString();

        if(mSelectedFilterCountry != null && mSelectedFilterCountry.getCountryName() != null)
            filterCountryId = mSelectedFilterCountry.getId();
        else
            filterCountryId = Utils.INVALID_ROW_INDEX;

        if(gender.equals(getString(R.string.all)))
           gender = null;

        mUsers                  = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(mCountries,filterCountryId, gender,null,mSearchCountryEdb.getText().toString(),true);

        mRecyclerAdapter.setUsersAndCountries(mUsers);
        mRecyclerAdapter.notifyDataSetChanged();

        checkSizeOfContactsAndSetMessage();

    }

    private void resetFilter()
    {
        mCountryEdb.setText(getString(R.string.all));
        mGenderRadioGroup.check(mGenderRadioGroup.getChildAt(0).getId());

        mFilterGender           = null;
        mSelectedFilterCountry  = null;
    }

    private void onSelectCountry()                                                      //start CountriesFragment
    {
        CountriesFragment countriesFragment = new CountriesFragment();
        Bundle            bundle            = new Bundle();

        bundle.putBoolean(ALL_COUNTRIES_ARE_SELECTED,true);
        countriesFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,0,0,R.anim.slide_up)
                .add(R.id.replace_layout,countriesFragment, Utils.COIUNTRIES_FRAGMENT_TAG).addToBackStack(Utils.COUNTRY_BACKSTACK_NAME).commit();
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

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//
//        if(getFragmentManager().getBackStackEntryCount() == 0) {
//            mTitleTxt.setText(getString(R.string.contacts));
//            mSearchCountryEdb.setText("");
//        }
//    }

    private void deleteUser(int position)                                                       //Delete user from database and listview
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(getActivity());

        if(usersDatabaseCommunication.deleteUserFromDatabase(mUsers.get(position))) {           //delete user from database, method return true if the query is successful
            mUsers.remove(position);
            mCountries.remove(position);
            mRecyclerAdapter.notifyDataSetChanged();

            if(mUsers.size() == 0) {
//                mSearchButton.setVisibility(View.GONE);
//                mFilterButton.setVisibility(View.GONE);
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

    @Override
    public void onBackStackChanged() {
        int backStackCount = getFragmentManager().getBackStackEntryCount();

//        if (getIntent() != null && getIntent().hasExtra(Utils.BUNDLE_COUNTRY_KEY) && getIntent().hasExtra(Utils.BUNDLE_USER_KEY))   // listener for backstack changing, in case there is intent with user and country for update, updating user and remove intent
//        {
//            UserModel user    = getIntent().getExtras().getParcelable(Utils.BUNDLE_USER_KEY);
//            CountryModel country = getIntent().getExtras().getParcelable(Utils.BUNDLE_COUNTRY_KEY);
//
//            if(getIntent().hasExtra(Utils.INTENT_REFRESH_USERS_KEY)) {
//                refreshUsers(user, country);
//                getIntent().removeExtra(Utils.INTENT_REFRESH_USERS_KEY);
//            }
//            else if(getIntent().hasExtra(Utils.INTENT_UPDATE_USER_KEY)) {
//                updateUser(user, country, getIntent().getExtras().getInt(Utils.BUNDLE_POSITION_KEY));
//                getIntent().removeExtra(Utils.INTENT_UPDATE_USER_KEY);
//            }
//
//            getIntent().removeExtra(Utils.BUNDLE_COUNTRY_KEY);
//            getIntent().removeExtra(Utils.BUNDLE_USER_KEY);
//
//            mTitleTxt.setText(getString(R.string.contacts));
//        }
//        else if(getIntent().getExtras() != null && getIntent().hasExtra(Utils.INTENT_FILTER_COUNTRY_KEY))
//        {
//            setFilterCountry((CountryModel) getIntent().getParcelableExtra(Utils.INTENT_FILTER_COUNTRY_KEY));
//            getIntent().removeExtra(Utils.INTENT_FILTER_COUNTRY_KEY);
//        }
//
//        if(getSupportFragmentManager().getBackStackEntryCount() > 0 && !getSupportFragmentManager().getBackStackEntryAt(backStackCount-1).getName().equals(Utils.FILTER_BACKSTACK_NAME)) {
//            mNewContactButton.setVisibility(View.GONE);
//            mSearchButton.setVisibility(View.GONE);
//            mFilterButton.setVisibility(View.GONE);
//        }
//        else {
//            if(((RelativeLayout.LayoutParams)mFilterLayout.getLayoutParams()).bottomMargin > 0)
//                mNewContactButton.setVisibility(View.VISIBLE);
//            if(mUsers.size() > 0) {
//                mFilterButton.setVisibility(View.VISIBLE);
//                mSearchButton.setVisibility(View.VISIBLE);
//            }
//        }
//
//        if(getSupportFragmentManager().getBackStackEntryCount() > 0 && getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName() != null
//                && getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1).getName().equals(Utils.COUNTRY_BACKSTACK_NAME) && !mStartAnimation)
//        {
//            mStartAnimation = true;
//            replaceElementsWithFade(mTitleTxt,mSearchCountryEdb);
//        }
//        else if(!mStartAnimation)
//        {
//            if(mSearchCountryEdb.getVisibility() == View.VISIBLE)
//                replaceElementsWithFade(mSearchCountryEdb,mTitleTxt);
//
//            mNotCountrySearching = true;
//            mSearchCountryEdb.setText("");
//        }
//        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(Utils.INTENT_DELETE_USER_POSITION))
//        {
//            deleteUser(getIntent().getExtras().getInt(Utils.INTENT_DELETE_USER_POSITION));
//            getIntent().removeExtra(Utils.INTENT_DELETE_USER_POSITION);
//        }
    }

    @Override
    public void onRecyclerItemClickListener(View view, int position) {
        int clickedViewId = view.getId();
        switch (clickedViewId)
        {
            case R.id.call_button:
                callToNumber(mCountries.get(position).getCallingCode(),mUsers.get(position));
                break;
            case R.id.user_item:
                onEdit(position);
                break;
            case R.id.user_image:
                selectImageForUser(position);
                break;
        }
    }

    private void selectImageForUser(int position)
    {
        mSelectedItemPosition = position;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK)
//        {
//            if (data != null)
//            {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//
//                if(cursor != null) {
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    Bitmap      bitmap          = BitmapFactory.decodeFile(picturePath);
//                    View        item            = mRecyclerView.getChildAt(mSelectedItemPosition);
//
//                    ImageView   image           = (ImageView) item.findViewById(R.id.user_image);
//                    image.setImageBitmap(bitmap);
//
//                    UsersDatabaseCommunication  usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);
//                    usersDatabaseCommunication.updateImageInDatabase(mUsers.get(mSelectedItemPosition).getId(),picturePath);
//                }
//            }
//        }
//        mSelectedItemPosition = Utils.INVALID_ROW_INDEX;
//    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
        RadioButton checkedButton = (RadioButton) radioGroup.findViewById(checkedButtonId);
        mFilterGender   = checkedButton.getText().toString();

        if(mFilterGender.equals(getString(R.string.all)))
            mFilterGender          = null;
    }

    private void checkSizeOfContactsAndSetMessage()
    {
        if(mUsers.size() == 0)
            mEmptyTxt.setVisibility(View.VISIBLE);
        else
            mEmptyTxt.setVisibility(View.GONE);
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
                    animateFilter(mFilterCurrentTopMargin, mFilterLayoutStartTopMargin);
                }
                break;
        }
        view.invalidate();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence,  int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        CountriesFragment countriesFragment = (CountriesFragment) getFragmentManager().findFragmentByTag(Utils.COIUNTRIES_FRAGMENT_TAG);
        if(countriesFragment != null && countriesFragment.isVisible())
        {
            countriesFragment.searchCountries(mSearchCountryEdb.getText().toString());
        }
        else if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            int filterCountryId;
            if(mSelectedFilterCountry != null && mSelectedFilterCountry.getCountryName() != null && !mSelectedFilterCountry.getCountryName().equals(""))
                filterCountryId = mSelectedFilterCountry.getId();
            else
                filterCountryId = Utils.INVALID_ROW_INDEX;

            if(!mNotCountrySearching) {
                mUsers = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(mCountries, filterCountryId, mFilterGender, null, mSearchCountryEdb.getText().toString(),true);
                mRecyclerAdapter.setUsersAndCountries(mUsers);
                mRecyclerAdapter.notifyDataSetChanged();

                checkSizeOfContactsAndSetMessage();
            }
            else
                mNotCountrySearching = false;
        }
    }

    private void loadCountries() throws MalformedURLException {
        CountriesDatabaseCommunication countryDatabaseCommunication   = CountriesDatabaseCommunication.getInstance(getActivity());

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
}
