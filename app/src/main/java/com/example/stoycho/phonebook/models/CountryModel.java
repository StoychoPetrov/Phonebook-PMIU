package com.example.stoycho.phonebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryModel implements Parcelable {

    private int     mId;
    private String  mCountryName;
    private String  mCallingCode;

    private final static String CALLING_CODE_JSON_KEY = "callingCodes";
    private final static String NAME_JSON_KEY         = "name";

    public CountryModel() {}

    public CountryModel(String countryName, String callingCode) {
        mCountryName   = countryName;
        mCallingCode   = callingCode;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public String getCallingCode() {
        return mCallingCode;
    }

    public void setCallingCode(String callingCode) {
        mCallingCode = callingCode;
    }

    private CountryModel(Parcel in)
    {
        mId             = in.readInt();
        mCountryName    = in.readString();
        mCallingCode    = in.readString();
    }

    private void parcefromJson(JSONObject country) throws JSONException {
        JSONArray countryCodes  = country.getJSONArray(CALLING_CODE_JSON_KEY);
        mCountryName            = country.getString(NAME_JSON_KEY);
        mCallingCode            = countryCodes.length() > 0 ? countryCodes.getString(0) : null;
    }

    public static List<CountryModel> parceCountriesFromJson(String countriesForParce) {
        List<CountryModel> countries = new ArrayList<>();
        try {
            JSONArray countriesJson = new JSONArray(countriesForParce);
            for (int i = 0; i < countriesJson.length(); i++) {
                CountryModel country = new CountryModel();
                country.parcefromJson(countriesJson.getJSONObject(i));
                countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mId);
        parcel.writeString(mCountryName);
        parcel.writeString(mCallingCode);
    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator() {
        @Override
        public Object createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return null;
        }

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new CountryModel(parcel);
        }

        @Override
        public Object[] newArray(int size) {
            return new CountryModel[size];
        }
    };
}