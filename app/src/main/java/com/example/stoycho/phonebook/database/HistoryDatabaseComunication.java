package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoycho.petrov on 21/02/2017.
 */

public class HistoryDatabaseComunication extends Database {

    private static HistoryDatabaseComunication instance  = null;

    public HistoryDatabaseComunication(Context context) {
        super(context);
    }

    public static HistoryDatabaseComunication getInstance(Context context)
    {
        if(instance == null)
            instance = new HistoryDatabaseComunication(context);
        return instance;
    }

    public long insertIntoHistoryTable(HistoryModel historyModel)
    {
        SQLiteDatabase db       = getWritableDatabase();

        ContentValues values    = new ContentValues();

        values.put(COLUMN_HISTORY_DATE,         historyModel.getmDate());
        values.put(COLUMN_USER_ID_FORIGN_KEY,   historyModel.getmUserId());

        if(historyModel.getmNotKnownPhone() != null)
            values.put(COLUMN_NOT_KNOWN_PHONE_NUMBER, historyModel.getmNotKnownPhone());

        values.put(COLUMN_STATE_ID_FOREIGN_KEY, historyModel.getmCallingStateId());

        long id = db.insert(HISTORY_TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public List<HistoryModel> selectAllHistory(String searchByName)
    {
        String query = "SELECT history." + COLUMN_HISTORY_ID + ",history." + COLUMN_HISTORY_DATE + ",history." + COLUMN_USER_ID_FORIGN_KEY + " ,history." + COLUMN_STATE_ID_FOREIGN_KEY + " ,history." + COLUMN_NOT_KNOWN_PHONE_NUMBER + " FROM " + HISTORY_TABLE_NAME + " history ";

        if(searchByName != null && !searchByName.equalsIgnoreCase(""))
            query += " JOIN " + USERS_TABLE_NAME + " users " + " ON history." + COLUMN_USER_ID_FORIGN_KEY + " = users." + COLUMN_USER_ID
                    + " WHERE users." + COLUMN_FIRST_NAME + " LIKE '" + searchByName + "%'";

        List<HistoryModel>     users    = new ArrayList<>();

        select(query,users);
        return users;
    }

    private void select(String query, List<HistoryModel> users)
    {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor   = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                HistoryModel historyModel = new HistoryModel();

                historyModel.setmHistoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_ID)));
                historyModel.setmDate(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_DATE)));
                historyModel.setmUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FORIGN_KEY)));
                historyModel.setmNotKnownPhone(cursor.getString(cursor.getColumnIndex(COLUMN_NOT_KNOWN_PHONE_NUMBER)));
                historyModel.setmCallingStateId(cursor.getInt(cursor.getColumnIndex(COLUMN_STATE_ID_FOREIGN_KEY)));

                users.add(historyModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

}
