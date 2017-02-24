package com.example.stoycho.phonebook.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.fragments.ContactsFragment;
import com.example.stoycho.phonebook.fragments.FavouritesFragment;
import com.example.stoycho.phonebook.fragments.HistoryFragment;

import java.util.List;

/**
 * Created by stoycho.petrov on 17/02/2017.
 */

    public class ViewPagerAdapter extends FragmentPagerAdapter {


    private static final int FAVOURITES_FRAGMENT_INDEX  = 0;
    private static final int HISTORY_FRAGMENT_INDEX     = 1;
    private static final int CONTACTS_FRAGMENT_INDEX    = 2;

    private FavouritesFragment  mFavouritesFragment;
    private HistoryFragment     mHistoryFragment;
    private ContactsFragment    mContactsFragment;

    private String[]     mTabsTitles;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTabsTitles = context.getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case FAVOURITES_FRAGMENT_INDEX:
                mFavouritesFragment =  new FavouritesFragment();
                return mFavouritesFragment;
            case HISTORY_FRAGMENT_INDEX:
                mHistoryFragment    = new HistoryFragment();
                return mHistoryFragment;
            case CONTACTS_FRAGMENT_INDEX:
                mContactsFragment   = new ContactsFragment();
                return mContactsFragment;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabsTitles[position];
    }

    @Override
    public int getCount() {
        return mTabsTitles.length;
    }

    public FavouritesFragment getmFavouritesFragment() {
        return mFavouritesFragment;
    }

    public HistoryFragment getmHistoryFragment() {
        return mHistoryFragment;
    }

    public ContactsFragment getmContactsFragment() {
        return mContactsFragment;
    }
}
