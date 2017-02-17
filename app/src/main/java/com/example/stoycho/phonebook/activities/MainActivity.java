package com.example.stoycho.phonebook.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.stoycho.phonebook.R;
import com.example.stoycho.phonebook.adapters.ViewPagerAdapter;


public class MainActivity extends FragmentActivity {

    private ViewPager       mViewPager;
    private TabLayout       mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initUI()
    {
        mViewPager  = (ViewPager)   findViewById(R.id.view_pager);
        mTabLayout  = (TabLayout)   findViewById(R.id.tabs_layout);
    }
}
