package com.example.weatherapp;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

//adapter for details activity tab fragments
public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;


    public PageAdapter (FragmentManager fm, int numOfTabs)
    {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    public Fragment getItem(int position)
    {
        switch(position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new WeeklyFragment();
            case 2:
                return new PhotosFragment();
            default:
                return null;
        }

    }

    public int getCount()
    {
        return this.numOfTabs;
    }



}
