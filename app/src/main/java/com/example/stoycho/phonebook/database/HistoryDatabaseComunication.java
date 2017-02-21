package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.HistoryModel;

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

        long id = db.insert(HISTORY_TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public List<HistoryModel> selectAllHistory()
    {
        String query = "SELECT * FROM " + HISTORY_TABLE_NAME;

        List<HistoryModel>     users    = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor   = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                HistoryModel historyModel = new HistoryModel();
                CountryModel country = new CountryModel();

                historyModel.setmHistoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_ID)));
                historyModel.setmDate(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_DATE)));
                historyModel.setmUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FORIGN_KEY)));

                users.add(historyModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return users;
    }

}
