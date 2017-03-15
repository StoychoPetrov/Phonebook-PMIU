package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by stoycho.petrov on 28/02/2017.
 */

public class CallingStatesDatabaseCommunication extends Database {


    private static CallingStatesDatabaseCommunication instance = null;

    private CallingStatesDatabaseCommunication(Context context) {
        super(context);
    }

    public static CallingStatesDatabaseCommunication getInstance(Context context)
    {
        if(instance == null)
            instance = new CallingStatesDatabaseCommunication(context);
        return instance;
    }

    public long insertCallingState(String callingState) {

        SQLiteDatabase db       = getWritableDatabase();
        ContentValues values    = new ContentValues();
        values.put(COLUMN_STATE_NAME, callingState);
        long resultFromQuery = db.insert(STATES_TABLE_NAME, null, values);
        db.close();

        return resultFromQuery;
    }

    public String getNameOfState(int stateId){

        String query = "SELECT " + COLUMN_STATE_NAME + " FROM " + STATES_TABLE_NAME
                + " WHERE " + COLUMN_STATE_ID + "=" + String.valueOf(stateId);

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor   = database.rawQuery(query, null);

        String stateName = null;

        if (cursor.moveToFirst()) {
           stateName    = cursor.getString(cursor.getColumnIndex(COLUMN_STATE_NAME));
        }

        cursor.close();
        database.close();

        return stateName;
    }

    public int getCountOfStates() {
        String query = "SELECT COUNT(*) FROM " + STATES_TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count;
    }
}
