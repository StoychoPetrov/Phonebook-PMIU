package com.example.stoycho.phonebook.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.UsersAndCountruesDatabaseComunication;
import com.example.stoycho.phonebook.database.UsersDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;
import com.example.stoycho.phonebook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText    mFirstNameEdb;
    private EditText    mLastNameEdb;
    private EditText    mCountryEdb;
    private EditText    mEmailEdb;
    private EditText    mPhoneNumberEdb;
    private TextView    mCallingCodeTxt;
    private TextView    mFirstNameTxt;
    private TextView    mLastNameTxt;
    private TextView    mEmailTxt;
    private TextView    mPhoneTxt;
    private RadioButton mMaleRadioBtn;
    private RadioButton mFemaleRadioBtn;
    private Button      mAddBtn;
    private Button      mDelete;
    private int         mCountryEdbId;
    private String      mPhoneCode;
    private boolean     mHasEmailError;
    private boolean     mHasPhoneError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        initUI(root);
        setListeners();

        if(getArguments() != null && getArguments().containsKey(Utils.BUNDLE_USER_KEY))
            setInformations();

        return root;
    }

    private void initUI(View root)
    {
        mFirstNameEdb       = (EditText)    root.findViewById(R.id.first_name);
        mLastNameEdb        = (EditText)    root.findViewById(R.id.last_name);
        mCountryEdb         = (EditText)    root.findViewById(R.id.country);
        mEmailEdb           = (EditText)    root.findViewById(R.id.email);
        mPhoneNumberEdb     = (EditText)    root.findViewById(R.id.phone_number);
        mCallingCodeTxt     = (TextView)    root.findViewById(R.id.callingCode);
        mFirstNameTxt       = (TextView)    root.findViewById(R.id.first_name_txt);
        mLastNameTxt        = (TextView)    root.findViewById(R.id.last_name_txt);
        mEmailTxt           = (TextView)    root.findViewById(R.id.email_txt);
        mPhoneTxt          = (TextView)    root.findViewById(R.id.phone_txt);
        mMaleRadioBtn       = (RadioButton) root.findViewById(R.id.male);
        mFemaleRadioBtn     = (RadioButton) root.findViewById(R.id.female);
        mAddBtn             = (Button)      root.findViewById(R.id.add);
        mDelete             = (Button)      root.findViewById(R.id.delete);
    }

    private void setListeners()
    {
        mAddBtn.setOnClickListener(this);
        mCountryEdb.setOnClickListener(this);
        mEmailEdb.setOnFocusChangeListener(this);
        mPhoneNumberEdb.setOnFocusChangeListener(this);
        mDelete.setOnClickListener(this);
        mFirstNameEdb.setOnFocusChangeListener(this);
        mLastNameEdb.setOnFocusChangeListener(this);
        mEmailEdb.setOnFocusChangeListener(this);
        mPhoneNumberEdb.setOnFocusChangeListener(this);
    }

    private void setInformations()
    {
        if(getArguments() != null) {

            CountryModel country = getArguments().getParcelable(Utils.BUNDLE_COUNTRY_KEY);
            UserModel user       = getArguments().getParcelable(Utils.BUNDLE_USER_KEY);

            if(user != null && country != null) {                                               // if user is not null, set user information in boxes.
                mFirstNameEdb.setText(user.getFirstName());
                mLastNameEdb.setText(user.getLastName());
                mEmailEdb.setText(user.getEmail());
                mCountryEdb.setText(country.getCountryName());
                mPhoneNumberEdb.setText(user.getPhoneNumber());

                mPhoneCode      = country.getCallingCode();
                String code     = mPhoneCode + " ";
                mCountryEdbId   = country.getId();

                if(mPhoneCode != null)
                {
                    mCallingCodeTxt.setText(code);
                    mCallingCodeTxt.setVisibility(View.VISIBLE);
                }

                String gender   = user.getGender();

                if (gender != null && gender.equals(getString(R.string.male)))
                    mMaleRadioBtn.setChecked(true);
                else
                    mFemaleRadioBtn.setChecked(true);
            }
        }

        mFirstNameEdb.setEnabled(false);
        mLastNameEdb.setEnabled(false);
        mCountryEdb.setEnabled(false);
        mEmailEdb.setEnabled(false);
        mPhoneNumberEdb.setEnabled(false);
        mMaleRadioBtn.setEnabled(false);
        mFemaleRadioBtn.setEnabled(false);
        mCallingCodeTxt.setEnabled(false);

        mAddBtn.setText(getString(R.string.edit));
    }

    @Override
    public void onClick(View clickedView) {
        int id = clickedView.getId();

        switch (id)
        {
            case R.id.add:
                if(mAddBtn.getText().toString().equals(getString(R.string.save))) {
                    mPhoneNumberEdb.clearFocus();
                    mEmailEdb.clearFocus();
                    if (getArguments() == null)
                        onAdd();
                    else
                        onEdit();
                }
                else
                    changeToEdit();
                break;
            case R.id.country:
                onCountry();
                break;
            case R.id.delete:
                deleteUser();
                break;
        }
    }

    private void deleteUser()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.deleteEntry))
                .setMessage(getString(R.string.deleteInfo))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getIntent().putExtra(Utils.INTENT_DELETE_USER_POSITION,getArguments().getInt(Utils.BUNDLE_POSITION_KEY));
                        getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void changeToEdit()
    {
        mFirstNameEdb.setEnabled(true);
        mLastNameEdb.setEnabled(true);
        mCountryEdb.setEnabled(true);
        mEmailEdb.setEnabled(true);
        mPhoneNumberEdb.setEnabled(true);
        mMaleRadioBtn.setEnabled(true);
        mFemaleRadioBtn.setEnabled(true);
        mCallingCodeTxt.setEnabled(true);

        mDelete.setVisibility(View.VISIBLE);
        mAddBtn.setText(getString(R.string.save));
    }

    private void onEdit()
    {
        if(!mFirstNameEdb.getText().toString().equals("") && !mLastNameEdb.getText().toString().equals("") && !mCountryEdb.getText().toString().equals("")
                && !mEmailEdb.getText().toString().equals("") && !mHasEmailError && !mHasPhoneError && (mMaleRadioBtn.isChecked() || mFemaleRadioBtn.isChecked()))                  // if every box is correct, will show dialog with question to user, in another case will show message with error.
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.updateInfo)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateUser();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            builder.create();
            builder.show();
        }
        else
            Toast.makeText(getActivity(),getString(R.string.registrationError),Toast.LENGTH_SHORT).show();
    }

    private void updateUser()
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(getActivity());

        UserModel user                                               = new UserModel(mFirstNameEdb.getText().toString(),mLastNameEdb.getText().toString(),mCountryEdbId,mEmailEdb.getText().toString(),
                                                                        mPhoneNumberEdb.getText().toString(),mMaleRadioBtn.isChecked() ? getString(R.string.male):getString(R.string.female));
        CountryModel country                                         = new CountryModel(mCountryEdb.getText().toString(),mPhoneCode);
        UserModel parcedUser                                         = getArguments().getParcelable(Utils.BUNDLE_USER_KEY);
        int userId;

        if(parcedUser != null)
            userId = parcedUser.getId();
        else
            userId = -1;

        user.setId(userId);
        if(usersDatabaseCommunication.updateUserInDatabase(user)) {                                     // if status of update query is siccess ,it will pop the fragment and show message. In other case will show error message.
            Toast.makeText(getActivity(), R.string.successUpdate, Toast.LENGTH_SHORT).show();
            getActivity().getIntent().putExtra(Utils.BUNDLE_USER_KEY,user);
            getActivity().getIntent().putExtra(Utils.BUNDLE_COUNTRY_KEY,country);
            getActivity().getIntent().putExtra(Utils.BUNDLE_POSITION_KEY,getArguments().getInt(Utils.BUNDLE_POSITION_KEY));
            getActivity().getIntent().putExtra(Utils.INTENT_UPDATE_USER_KEY,true);
            getFragmentManager().popBackStack();
        }
        else
            Toast.makeText(getActivity(),R.string.notSuccessEdit,Toast.LENGTH_SHORT).show();
    }

    private void onAdd()                                                                                                                                    //if every box is correct , it wil show question to user.
    {
        if(!mFirstNameEdb.getText().toString().equals("") && !mLastNameEdb.getText().toString().equals("") && !mCountryEdb.getText().toString().equals("")
                && !mEmailEdb.getText().toString().equals("") && !mHasEmailError && !mHasPhoneError && (mMaleRadioBtn.isChecked() || mFemaleRadioBtn.isChecked()))
        {
            List<UserModel> users = UsersAndCountruesDatabaseComunication.getInstance(getActivity()).selectUsersAndTheirCountries(new ArrayList<CountryModel>(),-1,null,mPhoneNumberEdb.getText().toString(),null);
            if(users.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.message_for_dialog)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                registerUser();                                                                                                             // if user press yes then try saving a new contact in database.
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            }
            else
                Toast.makeText(getActivity(),getString(R.string.sameNumber),Toast.LENGTH_SHORT).show();
        }
        else if(mHasEmailError)
        {
            Toast.makeText(getActivity(),R.string.invalid_email,Toast.LENGTH_SHORT).show();
        }
        else if(mHasPhoneError)
        {
            Toast.makeText(getActivity(),R.string.invalid_phone,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(),R.string.empty_box,Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser()
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(getActivity());

        UserModel user = new UserModel(mFirstNameEdb.getText().toString(),mLastNameEdb.getText().toString(),mCountryEdbId,mEmailEdb.getText().toString()
                ,mPhoneNumberEdb.getText().toString(),mMaleRadioBtn.isChecked() ? getString(R.string.male):getString(R.string.female));
        CountryModel country = new CountryModel(mCountryEdb.getText().toString(),mPhoneCode);
        long id = usersDatabaseCommunication.saveInDatabase(user);                                                      // Trying to save the new contact. If the query is successed, the id is different from -1. If it is equal to -1, there is error with query.
        if(id != -1) {
            user.setId((int) id);
            Toast.makeText(getActivity(), R.string.message_for_register, Toast.LENGTH_SHORT).show();
            getActivity().getIntent().putExtra(Utils.BUNDLE_USER_KEY,user);
            getActivity().getIntent().putExtra(Utils.BUNDLE_COUNTRY_KEY,country);
            getActivity().getIntent().putExtra(Utils.INTENT_REFRESH_USERS_KEY,true);
            getFragmentManager().popBackStack();
        }
        else
            Toast.makeText(getActivity(),R.string.register_error,Toast.LENGTH_SHORT).show();
    }

    private void onCountry()                                                                                            // User has selected country box and it will start CountryFragment
    {
        View viewFocus = getActivity().getCurrentFocus();

        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }

        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,0,0,R.anim.slide_up)
                .add(R.id.replace_layout,new CountriesFragment(), Utils.COIUNTRIES_FRAGMENT_TAG).addToBackStack(Utils.COUNTRY_BACKSTACK_NAME).commit();
    }

    public void setSelectedCountry(CountryModel country)
    {
        mCountryEdb.setText(country.getCountryName());
        mPhoneCode = country.getCallingCode();
        String code = mPhoneCode + " ";
        mCallingCodeTxt.setVisibility(View.VISIBLE);
        if(mPhoneCode != null) {
            mCallingCodeTxt.setText(code);
        }
        else
            mCallingCodeTxt.setVisibility(View.GONE);
        mCountryEdbId = country.getId();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.first_name:
                if (hasFocus) {
                    setFocusOfEditText(mFirstNameEdb,mFirstNameTxt);
                }
                else
                    removeFocus(mFirstNameEdb,mFirstNameTxt);
                break;
            case R.id.last_name:
                if (hasFocus) {
                    setFocusOfEditText(mLastNameEdb,mLastNameTxt);
                }
                else
                    removeFocus(mLastNameEdb,mLastNameTxt);
                break;
            case R.id.email:
                if (!hasFocus) {
                    removeFocus(mEmailEdb,mEmailTxt);
                    checkEmailValidation();
                }
                else
                {
                    setFocusOfEditText(mEmailEdb,mEmailTxt);
                }
                break;
            case R.id.phone_number:
                if (!hasFocus) {
                    checkPhoneValidation();
                    removeFocus(mPhoneNumberEdb,mPhoneTxt);
                }
                else
                    setFocusOfEditText(mPhoneNumberEdb,mPhoneTxt);
                break;
        }
    }

    private void checkPhoneValidation()
    {
        if(!android.util.Patterns.PHONE.matcher(mPhoneNumberEdb.getText().toString()).matches())
        {
            mPhoneNumberEdb.setError(getString(R.string.phone_error));
            mHasPhoneError = true;
        }
        else
            mHasPhoneError = false;
    }

    private void checkEmailValidation()
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(mEmailEdb.getText().toString()).matches())
        {
            mEmailEdb.setError(getString(R.string.email_error));
            mHasEmailError = true;
        }
        else
            mHasEmailError = false;
    }
}
