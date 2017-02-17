package com.example.stoycho.phonebook.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.stoycho.phonebook.models.CountryModel;
import com.example.stoycho.phonebook.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoycho.petrov on 07/12/2016.
 */

public class UsersAndCountruesDatabaseComunication extends Database {

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

    public final    static int                                      WITHOUT_COUNTRY_ID   = -1;
    private         static UsersAndCountruesDatabaseComunication    instance             = null;

    private UsersAndCountruesDatabaseComunication(Context context) {
        super(context);
    }

    public List<UserModel> selectUsersAndTheirCountries(List<CountryModel> countries, int countryId, String gender, String phone, String filterByName)
    {

        boolean hasWhereClause = false;

        String query = "SELECT * "
                +  "FROM " + USERS_TABLE_NAME + " users " + "INNER JOIN " + COUNTRIES_TABLE_NAME + " countries "
                +  "ON " + "users." + COLUMN_COUNTRY_ID_FK + " = countries." + COLUMN_COUNTRY_ID;
        if(countryId >= 0 && gender == null) {
            query += " WHERE users." + COLUMN_COUNTRY_ID_FK + " = " + countryId;
            hasWhereClause = true;
        }
        else if(countryId < 0 && gender != null) {
            query += " WHERE users." + COLUMN_GENDER + " = '" + gender + "'";
            hasWhereClause = true;
        }
        else if(countryId >=0 && gender != null) {
            query += " WHERE users." + COLUMN_COUNTRY_ID_FK + " = " + countryId + " AND users." + COLUMN_GENDER + " = '" + gender + "'";
            hasWhereClause = true;
        }
        else if(phone != null) {
            query += " WHERE users." + COLUMN_PHONE_NUMBER + " = " + phone;
            hasWhereClause = true;
        }

        if(filterByName != null && !filterByName.equals("") && hasWhereClause)
        {
            query += " AND users." + COLUMN_FIRST_NAME + " LIKE '" + filterByName + "%'";
        }
        else if(filterByName != null && !filterByName.equals(""))
        {
            query += " WHERE users." + COLUMN_FIRST_NAME + " LIKE '" + filterByName + "%'";
        }


        List<UserModel>     users    = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor         cursor   = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                CountryModel country = new CountryModel();

                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                user.setmImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                user.setCountry(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID_FK)));
                users.add(user);

                country.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID)));
                country.setCountryName(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_NAME)));
                country.setCallingCode(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_CODE)));
                countries.add(country);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return users;
    }

    public static UsersAndCountruesDatabaseComunication getInstance(Context context)
    {
        if(instance == null)
            instance = new UsersAndCountruesDatabaseComunication(context);
        return instance;
    }
}
