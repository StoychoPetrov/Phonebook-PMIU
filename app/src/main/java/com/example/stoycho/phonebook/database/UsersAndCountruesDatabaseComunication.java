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


    public final    static int                                      WITHOUT_COUNTRY_ID   = -1;
    private         static UsersAndCountruesDatabaseComunication    instance             = null;

    private boolean mOrderByCallingCount;

    private UsersAndCountruesDatabaseComunication(Context context) {
        super(context);
    }

    public List<UserModel> selectUsersAndTheirCountries(List<CountryModel> countries, int countryId, String gender, String phone, String filterByName,boolean orderByName)
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

        if(orderByName)
            query += " ORDER BY " + COLUMN_FIRST_NAME;

        if(mOrderByCallingCount)
            query += " ORDER BY " + COLUMN_CALLS_COUNT + " DESC ";

        List<UserModel> users = new ArrayList<>();

        select(query,countries,users);

        return users;
    }

    public UserModel selectUserById(int userId,CountryModel countryModel)
    {
        String query = "SELECT * "
            +  "FROM " + USERS_TABLE_NAME + " users " + "INNER JOIN " + COUNTRIES_TABLE_NAME + " countries "
            +  "ON " + "users." + COLUMN_COUNTRY_ID_FK + " = countries." + COLUMN_COUNTRY_ID + " WHERE users." + COLUMN_USER_ID + " = " + userId;

        List<UserModel>     users       = new ArrayList<>();
        List<CountryModel>  countries   = new ArrayList<>();

        select(query,countries,users);

        if(countries.size() > 0 )
            countryModel    = countries.get(0);

        if(users.size() > 0)
            return users.get(0);
        else
            return null;
    }

    private void select(String query,List<CountryModel> countries, List<UserModel> users)
    {
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
                user.setmCallsCount(cursor.getInt(cursor.getColumnIndex(COLUMN_CALLS_COUNT)));
                users.add(user);

                country.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNTRY_ID)));
                country.setCountryName(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_NAME)));
                country.setCallingCode(cursor.getString(cursor.getColumnIndex(COLUMN_CALLING_CODE)));
                countries.add(country);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
    }

    public static UsersAndCountruesDatabaseComunication getInstance(Context context)
    {
        if(instance == null)
            instance = new UsersAndCountruesDatabaseComunication(context);
        return instance;
    }

    public void setOrderByCallingCount(boolean orderByCallingCount)
    {
        mOrderByCallingCount = orderByCallingCount;
    }
}
