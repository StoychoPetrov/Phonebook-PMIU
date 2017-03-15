package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

/**
 * Created by stoycho.petrov on 07/12/2016.
 */

public class UsersDatabaseCommunication extends Database {

    private static UsersDatabaseCommunication instance  = null;

    private UsersDatabaseCommunication(Context context) {
        super(context);
    }

    public static UsersDatabaseCommunication getInstance(Context context)
    {
        if(instance == null)
            instance = new UsersDatabaseCommunication(context);
        return instance;
    }

    public long saveInDatabase(UserModel user) {
        SQLiteDatabase db       = getWritableDatabase();
        ContentValues values    = new ContentValues();
        values.put(COLUMN_FIRST_NAME,       user.getFirstName());
        values.put(COLUMN_LAST_NAME,        user.getLastName());
        values.put(COLUMN_EMAIL,            user.getEmail());
        values.put(COLUMN_PHONE_NUMBER,     user.getPhoneNumber());
        values.put(COLUMN_GENDER,           user.getGender());
        values.put(COLUMN_CALLS_COUNT,      0);
        values.put(COLUMN_COUNTRY_ID_FK,    user.getCountry());
        long id = db.insert(USERS_TABLE_NAME, null, values);
        db.close();
        user.setId((int) id);
        return id;
    }

    public boolean updateUserInDatabase(UserModel user)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values    = new ContentValues();

        values.put(COLUMN_FIRST_NAME,user.getFirstName());
        values.put(COLUMN_LAST_NAME,user.getLastName());
        values.put(COLUMN_EMAIL,user.getEmail());
        values.put(COLUMN_PHONE_NUMBER,user.getPhoneNumber());
        values.put(COLUMN_GENDER,user.getGender());
        values.put(COLUMN_COUNTRY_ID_FK,user.getCountry());

        int result = database.update(USERS_TABLE_NAME,values,COLUMN_USER_ID + "=?",new String[]{String.valueOf(user.getId())});
        database.close();

        return result > 0;
    }

    public boolean deleteUserFromDatabase(UserModel user)
    {
        SQLiteDatabase  database = getWritableDatabase();
        boolean         result   = database.delete(USERS_TABLE_NAME,COLUMN_USER_ID + "=?",new String[]{String.valueOf(user.getId())}) > 0;

        database.close();
        return result;
    }

    public boolean updateImageInDatabase(int userId,String imagePath)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imagePath);

        int result = database.update(USERS_TABLE_NAME,values,COLUMN_USER_ID + "=?",new String[]{String.valueOf(userId)});
        database.close();

        return result > 0;
    }

    public boolean updateCallsCounts(int userId, int callCount)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CALLS_COUNT, callCount);
        int result = database.update(USERS_TABLE_NAME,values,COLUMN_USER_ID + "=?",new String[]{String.valueOf(userId)});
        database.close();

        return result > 0;
    }

    public UserModel getUserIdByPhone(String phoneNumber) {

        UserModel userModel = new UserModel();
        String query = "SELECT " + COLUMN_USER_ID + "," + COLUMN_CALLS_COUNT + ", " + COLUMN_FIRST_NAME + " FROM " + USERS_TABLE_NAME
                + " WHERE " + COLUMN_PHONE_NUMBER + " LIKE " + phoneNumber;

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor   = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            userModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            userModel.setmCallsCount(cursor.getInt(cursor.getColumnIndex(COLUMN_CALLS_COUNT)));
            userModel.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
        }
        else
            userModel   = null;

        cursor.close();
        database.close();

        return userModel;
    }
}
