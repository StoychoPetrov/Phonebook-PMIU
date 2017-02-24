package com.example.stoycho.phonebook.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stoycho on 10/20/2016.
 */

public class UserModel implements Parcelable{

    private int     mId;
    private String  mFirstName;
    private String  mLastName;
    private int     mCountryId;
    private String  mEmail;
    private String  mPhoneNumber;
    private String  mGender;
    private String  mImage;
    private int     mCallsCount;

    public UserModel() {
    }

    public UserModel(String firstName, String lastName, int countryId, String email, String phoneNumber, String gender, int callsCount) {
        mFirstName     = firstName;
        mLastName      = lastName;
        mCountryId     = countryId;
        mEmail         = email;
        mPhoneNumber   = phoneNumber;
        mGender        = gender;
        mCallsCount    = callsCount;

    }

    private UserModel(Parcel in)
    {
        mId            = in.readInt();
        mFirstName     = in.readString();
        mLastName      = in.readString();
        mCountryId     = in.readInt();
        mEmail         = in.readString();
        mPhoneNumber   = in.readString();
        mGender        = in.readString();
        mImage         = in.readString();
        mCallsCount    = in.readInt();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public int getCountry() {
        return mCountryId;
    }

    public void setCountry(int countryId) {
        mCountryId = countryId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String male) {
        mGender = male;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public int getmCallsCount() {
        return mCallsCount;
    }

    public void setmCallsCount(int mCallsCount) {
        this.mCallsCount = mCallsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mId);
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
        parcel.writeInt(mCountryId);
        parcel.writeString(mEmail);
        parcel.writeString(mPhoneNumber);
        parcel.writeString(mGender);
        parcel.writeString(mImage);
        parcel.writeInt(mCallsCount);
    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator() {
        @Override
        public Object createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new UserModel(parcel);
        }

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new UserModel(parcel);
        }

        @Override
        public Object[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
