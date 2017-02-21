package com.example.stoycho.phonebook.models;

/**
 * Created by stoycho.petrov on 21/02/2017.
 */

public class HistoryModel {

    private int         mHistoryId;
    private String      mDate;
    private int         mUserId;

    public HistoryModel(){}

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
}
