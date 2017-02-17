package com.example.stoycho.phonebook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stoycho on 10/20/2016.
 */

public class Database extends SQLiteOpenHelper {

    /********** Database info ************/
    private static final String DATABASE_NAME           = "phonebook";
    private static final int DATABASE_VERSION           = 1;

    /********** Table names **************/
    private final static String USERS_TABLE_NAME        = "users";
    private final static String COUNTRIES_TABLE_NAME    = "countries";

    /********** Users table columns ************/
    private final static String COLUMN_USER_ID          = "user_id";
    private final static String COLUMN_FIRST_NAME       = "first_name";
    private final static String COLUMN_LAST_NAME        = "last_name";
    private final static String COLUMN_EMAIL            = "email";
    private final static String COLUMN_PHONE_NUMBER     = "phone_number";
    private final static String COLUMN_GENDER           = "gender";
    private final static String COLUMN_IMAGE            = "image";
    private final static String COLUMN_COUNTRY_ID_FK    = "coutry_id_fk";

    /*********** Countries table columns*********/
    private final static String COLUMN_COUNTRY_ID       = "country_id";
    private final static String COLUMN_COUNTRY_NAME     = "country_name";
    private final static String COLUMN_CALLING_CODE     = "calling_code";

    /*********** Create tables *******************/
    private final static String CREATE_USERS = " CREATE TABLE " + USERS_TABLE_NAME + " ( " + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE_NUMBER + " TEXT, " + COLUMN_GENDER + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_COUNTRY_ID_FK + " INTEGER, FOREIGN KEY (" + COLUMN_COUNTRY_ID_FK + ") REFERENCES " + COUNTRIES_TABLE_NAME
            + "(" + COLUMN_COUNTRY_ID + "))";
    private final static String CREATE_COUNTRIES = " CREATE TABLE " + COUNTRIES_TABLE_NAME + " ( " + COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COUNTRY_NAME + " TEXT, "
            + COLUMN_CALLING_CODE + " TEXT)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_COUNTRIES);
        database.execSQL(CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);
            onCreate(database);
        }
    }
}
