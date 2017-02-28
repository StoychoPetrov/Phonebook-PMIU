package com.example.stoycho.phonebook.models;

/**
 * Created by stoycho.petrov on 21/02/2017.
 */

public class HistoryModel {

    private int         mHistoryId;
    private String      mDate;
    private int         mUserId;
    private String      mHeather;
    private int         mType;
    private int         mCallingStateId;
    private String      mNotKnownPhone;
    private String      mCallingState;

    public HistoryModel(){
        mUserId = -1;
    }

    public HistoryModel(String mDate, int mUserId) {
        this.mDate          = mDate;
        this.mUserId        = mUserId;
    }

    public int getmHistoryId() {
        return mHistoryId;
    }

    public void setmHistoryId(int mHistoryId) {
        this.mHistoryId = mHistoryId;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmHeather() {
        return mHeather;
    }

    public void setmHeather(String mHeather) {
        this.mHeather = mHeather;
    }

    public String getmCallingState() {
        return mCallingState;
    }

    public void setmCallingState(String mCallingState) {
        this.mCallingState = mCallingState;
    }

    public int getmCallingStateId() {
        return mCallingStateId;
    }

    public void setmCallingStateId(int mCallingStateId) {
        this.mCallingStateId = mCallingStateId;
    }

    public String getmNotKnownPhone() {
        return mNotKnownPhone;
    }

    public void setmNotKnownPhone(String mNotKnownPhone) {
        this.mNotKnownPhone = mNotKnownPhone;
    }

}
