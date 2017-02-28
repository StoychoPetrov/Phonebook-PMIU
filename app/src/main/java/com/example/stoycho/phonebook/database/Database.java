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
    protected final static String USERS_TABLE_NAME        = "users";
    protected final static String COUNTRIES_TABLE_NAME    = "countries";
    protected final static String HISTORY_TABLE_NAME      = "histories";

    /********** Users table columns ************/
    protected final static String COLUMN_USER_ID          = "user_id";
    protected final static String COLUMN_FIRST_NAME       = "first_name";
    protected final static String COLUMN_LAST_NAME        = "last_name";
    protected final static String COLUMN_EMAIL            = "email";
    protected final static String COLUMN_PHONE_NUMBER     = "phone_number";
    protected final static String COLUMN_GENDER           = "gender";
    protected final static String COLUMN_IMAGE            = "image";
    protected final static String COLUMN_CALLS_COUNT      = "calls_count";
    protected final static String COLUMN_COUNTRY_ID_FK    = "coutry_id_fk";

    /*********** Countries table columns*********/
    protected final static String COLUMN_COUNTRY_ID       = "country_id";
    protected final static String COLUMN_COUNTRY_NAME     = "country_name";
    protected final static String COLUMN_CALLING_CODE     = "calling_code";

    /*********** History table columns ***********/
    protected final static String COLUMN_HISTORY_ID             = "history_id";
    protected final static String COLUMN_HISTORY_DATE           = "history_date";
    protected final static String COLUMN_USER_ID_FORIGN_KEY     = "user_id";
    protected final static String COLUMN_NOT_KNOWN_PHONE_NUMBER = "not_known_phone";
    protected final static String COLUMN_STATE_ID_FOREIGN_KEY   = "calling_state";

    /*********** Phone calls states ***************/
    protected final static String STATES_TABLE_NAME             = "calling_states";
    protected final static String COLUMN_STATE_ID               = "state_id";
    protected final static String COLUMN_STATE_NAME             = "state_name";

    /*********** Create tables *******************/
    private final static String CREATE_USERS = " CREATE TABLE " + USERS_TABLE_NAME + " ( " + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE_NUMBER + " TEXT, " + COLUMN_GENDER + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_CALLS_COUNT + " INTEGER, " + COLUMN_COUNTRY_ID_FK + " INTEGER, FOREIGN KEY (" + COLUMN_COUNTRY_ID_FK + ") REFERENCES " + COUNTRIES_TABLE_NAME
            + "(" + COLUMN_COUNTRY_ID + "))";

    private final static String CREATE_COUNTRIES = " CREATE TABLE " + COUNTRIES_TABLE_NAME + " ( " + COLUMN_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_COUNTRY_NAME + " TEXT, "
            + COLUMN_CALLING_CODE + " TEXT)";

    private final static String CREATE_HISTORY   = " CREATE TABLE " + HISTORY_TABLE_NAME + " ( " + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_HISTORY_DATE + " TEXT, " + COLUMN_USER_ID_FORIGN_KEY + " INTEGER, " + COLUMN_STATE_ID_FOREIGN_KEY + " INTEGER, " + COLUMN_NOT_KNOWN_PHONE_NUMBER + " TEXT, "
            + " FOREIGN KEY (" + COLUMN_USER_ID_FORIGN_KEY + ") REFERENCES " + USERS_TABLE_NAME + "(" + COLUMN_USER_ID + "), "
            + " FOREIGN KEY (" + COLUMN_STATE_ID_FOREIGN_KEY + ") REFERENCES "  + STATES_TABLE_NAME + "(" + COLUMN_STATE_ID + "));";


    private final static String CREATE_STATES    = " CREATE TABLE " + STATES_TABLE_NAME  + " ( " + COLUMN_STATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STATE_NAME + " TEXT)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_COUNTRIES);
        database.execSQL(CREATE_USERS);
        database.execSQL(CREATE_STATES);
        database.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + STATES_TABLE_NAME);
            database.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
            onCreate(database);
        }
    }
}
