package com.example.stoycho.phonebook.utils;

import android.content.Context;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.database.CountriesDatabaseCommunication;
import com.example.stoycho.phonebook.models.CountryModel;

import java.util.List;

/**
 * Created by stoycho.petrov on 14/12/2016.
 */

public class Utils {

    public final static String URL_FOR_COUNTRIES                        = "https://restcountries.eu/rest/v1/all";

    public final static String BUNDLE_USER_KEY                          = "user";
    public final static String BUNDLE_COUNTRY_KEY                       = "country";
    public final static String BUNDLE_POSITION_KEY                      = "position";

    public final static String REGISTRATION_BACKSTACK_NAME              = "registrationBackstack";
    public final static String COUNTRY_BACKSTACK_NAME                   = "countryBackstack";
    public final static String FILTER_BACKSTACK_NAME                    = "filterBackstack";

    public final static String REGISTRATION_FRAGMENT_TAG                = "registerFragment";
    public final static String COIUNTRIES_FRAGMENT_TAG                  = "countryFragment";

    public final static String INTENT_FILTER_COUNTRY_KEY                = "filter_country";
    public final static String INTENT_REFRESH_USERS_KEY                 = "refresh_users";
    public final static String INTENT_UPDATE_USER_KEY                   = "update_user";
    public final static String INTENT_DELETE_USER_POSITION              = "delete_user";

    public final static int INVALID_ROW_INDEX                           =  -1;

    /************** STATES IDS ************************/
    public final static int STATE_OUTGOING                              = 0;
    public final static int STATE_INCOMMING                             = 1;
    public final static int STATE_MISSED                                = 2;


    //  JSON COUNTRY KEYS
    public final static String JSON_COUNTRY_NAME_KEY                    = "name";
    public final static String JSON_COUNTRY_CALLING_CODE_KEY            = "callingCodes";

    //RESULT CODES
    public final static int     COUNTRY_REQUEST_CODE                     = 1;
    public final static int     ADD_CONTACT_REQUEST_CODE                 = 2;
    public final static int     EDIT_CONTACT_REQUEST_CODE                = 3;


    public static String getColor(Context context,String letter)
    {
        String colors[]                = context.getResources().getStringArray(R.array.favourite_colors);
        int index = 0;
        for (char i = 'A'; i <= 'Z'; i ++)
        {
            if(letter.equalsIgnoreCase(String.valueOf(i)))
                return colors[index];
            index++;
        }
        return null;
    }

    public static String getCountryCode(Context context, String phone){

        CountriesDatabaseCommunication countriesDatabaseCommunication = CountriesDatabaseCommunication.getInstance(context);
        List<CountryModel> countries                      = countriesDatabaseCommunication.selectAllCountriesFromDatabase(CountriesDatabaseCommunication.SELECT_ALL_COUNTRIES,null);

        for(CountryModel countryModel : countries) {
            if (countryModel.getCallingCode() != null && !countryModel.getCallingCode().equalsIgnoreCase("")) {
                if (phone.contains("+" + countryModel.getCallingCode()))
                    return countryModel.getCallingCode();
            }
        }
        return null;
    }
}
