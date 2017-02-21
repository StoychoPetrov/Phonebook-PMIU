package com.example.stoycho.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.CountryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoycho.petrov on 07/12/2016.
 */

public class CountriesDatabaseCommunication extends Database {

    public final static int SELECT_ALL_COUNTRIES        = 0;
    public final static int SELECT_SEARCH_PLACES        = 1;

    private static CountriesDatabaseCommunication instance = null;

    private CountriesDatabaseCommunication(Context context) {
        super(context);
    }

    public static CountriesDatabaseCommunication getInstance(Context context)
    {
        if(instance == null)
            instance = new CountriesDatabaseCommunication(context);
        return instance;
    }

    public long saveInDatabase(CountryModel country)
    {
        SQLiteDatabase db       = getWritableDatabase();
        ContentValues values    = new ContentValues();
        values.put(COLUMN_COUNTRY_NAME, country.getCountryName());
        values.put(COLUMN_CALLING_CODE, country.getCallingCode());
        long resultFromQuery = db.insert(COUNTRIES_TABLE_NAME, null, values);
        db.close();
        return resultFromQuery;
    }

    public List<CountryModel> selectAllCountriesFromDatabase(int selectQuery, String nameForWhereClause)
    {
        String query = null;
        List<CountryModel> countries = new ArrayList<>();
        if(selectQuery == SELECT_ALL_COUNTRIES)
            query = "SELECT * FROM " + COUNTRIES_TABLE_NAME;
        else if(selectQuery == SELECT_SEARCH_PLACES)
            query = "SELECT * FROM " + COUNTRIES_TABLE_NAME
                    + " WHERE " + COLUMN_COUNTRY_NAME + " LIKE " + "'" + nameForWhereClause + "%'";
        if(query != null) {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    CountryModel country = new CountryModel();
                    country.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID)));
                    country.setCountryName(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_NAME)));
                    country.setCallingCode(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_CODE)));
                    countries.add(country);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return countries;
    }

    public int getCountOfCountries()
    {
        String query = "SELECT COUNT(*) FROM " + COUNTRIES_TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count;
    }
}
