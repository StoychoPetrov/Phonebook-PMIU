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

    public CallingStatesDatabaseCommunication(Context context) {
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
