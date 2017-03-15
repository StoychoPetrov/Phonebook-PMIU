package com.example.stoycho.phonebook.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
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

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener,EditText.OnKeyListener {

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
    private int         mCountryId;
    private String      mPhoneCode;
    private boolean     mHasEmailError;
    private boolean     mHasPhoneError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        
        initUI();
        setListeners();

        if(getIntent() != null && getIntent().hasExtra(Utils.BUNDLE_USER_KEY))
            setInformations();
    }
    
    private void initUI() {
        mFirstNameEdb       = (EditText)    findViewById(R.id.first_name);
        mLastNameEdb        = (EditText)    findViewById(R.id.last_name);
        mCountryEdb         = (EditText)    findViewById(R.id.country);
        mEmailEdb           = (EditText)    findViewById(R.id.email);
        mPhoneNumberEdb     = (EditText)    findViewById(R.id.phone_number);
        mCallingCodeTxt     = (TextView)    findViewById(R.id.callingCode);
        mFirstNameTxt       = (TextView)    findViewById(R.id.first_name_txt);
        mLastNameTxt        = (TextView)    findViewById(R.id.last_name_txt);
        mEmailTxt           = (TextView)    findViewById(R.id.email_txt);
        mPhoneTxt           = (TextView)    findViewById(R.id.phone_txt);
        mMaleRadioBtn       = (RadioButton) findViewById(R.id.male);
        mFemaleRadioBtn     = (RadioButton) findViewById(R.id.female);
        mAddBtn             = (Button)      findViewById(R.id.add);
        mDelete             = (Button)      findViewById(R.id.delete);
    }

    private void setListeners() {
        mAddBtn.setOnClickListener(this);
        mCountryEdb.setOnClickListener(this);
        mEmailEdb.setOnFocusChangeListener(this);
        mPhoneNumberEdb.setOnFocusChangeListener(this);
        mDelete.setOnClickListener(this);
        mFirstNameEdb.setOnFocusChangeListener(this);
        mLastNameEdb.setOnFocusChangeListener(this);
        mEmailEdb.setOnFocusChangeListener(this);
        mPhoneNumberEdb.setOnFocusChangeListener(this);
        mPhoneNumberEdb.setOnKeyListener(this);
    }

    private void setInformations() {
        if(getIntent() != null) {

            CountryModel    country     = getIntent().getParcelableExtra(Utils.BUNDLE_COUNTRY_KEY);
            UserModel       user        = getIntent().getParcelableExtra(Utils.BUNDLE_USER_KEY);

            if(user != null && country != null) {       // if user is not null, set user information in boxes.

                setFocusOfEditText(mFirstNameEdb,mFirstNameTxt);
                setFocusOfEditText(mLastNameEdb,mLastNameTxt);
                setFocusOfEditText(mEmailEdb,mEmailTxt);
                setFocusOfEditText(mPhoneNumberEdb,mPhoneTxt);

                mFirstNameEdb.setText(user.getFirstName());
                mLastNameEdb.setText(user.getLastName());
                mEmailEdb.setText(user.getEmail());
                mCountryEdb.setText(country.getCountryName());
                mPhoneNumberEdb.setText(user.getPhoneNumber());

                mPhoneCode      = country.getCallingCode();
                String code     = mPhoneCode + " ";
                mCountryId   = country.getId();

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
                    if (!getIntent().hasExtra(Utils.BUNDLE_USER_KEY))
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

    private void deleteUser() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.deleteEntry))
                .setMessage(getString(R.string.deleteInfo))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getIntent().putExtra(Utils.INTENT_DELETE_USER_POSITION,getIntent().getIntExtra(Utils.BUNDLE_POSITION_KEY,-1));
                        getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void changeToEdit() {
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

    private void onEdit() {
        if(!mFirstNameEdb.getText().toString().equals("") && !mLastNameEdb.getText().toString().equals("") && !mCountryEdb.getText().toString().equals("")
                && !mEmailEdb.getText().toString().equals("") && !mHasEmailError && !mHasPhoneError && (mMaleRadioBtn.isChecked() || mFemaleRadioBtn.isChecked()))                  // if every box is correct, will show dialog with question to user, in another case will show message with error.
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            Toast.makeText(this,getString(R.string.registrationError),Toast.LENGTH_SHORT).show();
    }

    private void updateUser() {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);

        UserModel user                                               = new UserModel(mFirstNameEdb.getText().toString(),mLastNameEdb.getText().toString(),mCountryId,mEmailEdb.getText().toString(),
                                                                        mPhoneNumberEdb.getText().toString(),mMaleRadioBtn.isChecked() ? getString(R.string.male):getString(R.string.female),0);
        CountryModel country                                         = new CountryModel(mCountryEdb.getText().toString(),mPhoneCode);
        UserModel parcedUser                                         = getIntent().getParcelableExtra(Utils.BUNDLE_USER_KEY);
        int userId;

        if(parcedUser != null)
            userId = parcedUser.getId();
        else
            userId = -1;

        user.setId(userId);
        if(usersDatabaseCommunication.updateUserInDatabase(user)) {     // if status of update query is siccess ,it will pop the fragment and show message. In other case will show error message.
            Toast.makeText(this, R.string.successUpdate, Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent();
            intent.putExtra(Utils.BUNDLE_USER_KEY,user);
            intent.putExtra(Utils.BUNDLE_COUNTRY_KEY,country);
            intent.putExtra(Utils.BUNDLE_POSITION_KEY,getIntent().getIntExtra(Utils.BUNDLE_POSITION_KEY,-1));
            setResult(RESULT_OK,intent);
            finish();
        }
        else
            Toast.makeText(this,R.string.notSuccessEdit,Toast.LENGTH_SHORT).show();
    }

    private void onAdd(){                                                                                                                                    //if every box is correct , it wil show question to user.

        if(!mFirstNameEdb.getText().toString().equals("") && !mLastNameEdb.getText().toString().equals("") && !mCountryEdb.getText().toString().equals("")
                && !mEmailEdb.getText().toString().equals("") && !mHasEmailError && !mHasPhoneError && (mMaleRadioBtn.isChecked() || mFemaleRadioBtn.isChecked()))
        {
            List<UserModel> users = UsersAndCountruesDatabaseComunication.getInstance(this).selectUsersAndTheirCountries(new ArrayList<CountryModel>(),-1,null,mPhoneNumberEdb.getText().toString(),null,false);
            if(users.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                Toast.makeText(this,getString(R.string.sameNumber),Toast.LENGTH_SHORT).show();
        }
        else if(mHasEmailError)
        {
            Toast.makeText(this,R.string.invalid_email,Toast.LENGTH_SHORT).show();
        }
        else if(mHasPhoneError)
        {
            Toast.makeText(this,R.string.invalid_phone,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,R.string.empty_box,Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser()
    {
        UsersDatabaseCommunication usersDatabaseCommunication = UsersDatabaseCommunication.getInstance(this);

        UserModel user = new UserModel(mFirstNameEdb.getText().toString(),mLastNameEdb.getText().toString(),mCountryId,mEmailEdb.getText().toString()
                ,mPhoneNumberEdb.getText().toString().substring(mPhoneCode.length()),mMaleRadioBtn.isChecked() ? getString(R.string.male):getString(R.string.female),0);
        CountryModel country = new CountryModel(mCountryEdb.getText().toString(),mPhoneCode);
        long id = usersDatabaseCommunication.saveInDatabase(user);                                                      // Trying to save the new contact. If the query is successed, the id is different from -1. If it is equal to -1, there is error with query.
        if(id != -1) {

            user.setId((int) id);
            Toast.makeText(this, R.string.message_for_register, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra(Utils.BUNDLE_USER_KEY,user);
            intent.putExtra(Utils.BUNDLE_COUNTRY_KEY,country);

            setResult(RESULT_OK,intent);
            finish();
        }
        else
            Toast.makeText(this,R.string.register_error,Toast.LENGTH_SHORT).show();
    }

    private void onCountry()                                                                                            // User has selected country box and it will start CountryFragment
    {
        View viewFocus = this.getCurrentFocus();

        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }

        Intent countriesActivity = new Intent(this,CountriesActivity.class);
        startActivityForResult(countriesActivity,Utils.COUNTRY_REQUEST_CODE);
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
                else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Utils.COUNTRY_REQUEST_CODE)
        {
            CountryModel countryModel = data.getExtras().getParcelable(Utils.BUNDLE_COUNTRY_KEY);
            if(countryModel != null) {
                findViewById(R.id.country_txt).setVisibility(View.VISIBLE);

                mCountryId  = countryModel.getId();
                mPhoneCode  = countryModel.getCallingCode();

                if(mPhoneNumberEdb.getText().toString().equalsIgnoreCase(""))
                    mPhoneNumberEdb.setText("");
                else
                    setFocusOfEditText(mPhoneNumberEdb,mPhoneTxt);

                setFocusOfEditText(mPhoneNumberEdb,mPhoneTxt);
                mPhoneNumberEdb.setText("+" + countryModel.getCallingCode());
                mCountryEdb.setText(countryModel.getCountryName());
            }
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(view.getId() == mPhoneNumberEdb.getId() && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL){
            if(("+" + mPhoneCode).equalsIgnoreCase(mPhoneNumberEdb.getText().toString())){
                return true;
            }
        }
        return false;
    }
}
