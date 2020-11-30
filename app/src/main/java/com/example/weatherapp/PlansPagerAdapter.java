package com.example.weatherapp;


import org.json.JSONObject;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

//adapter for favorites tab fragments
public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private ArrayList<String> json;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    TabLayout tab;
    boolean removing;

    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> json, JSONObject currLocJson, TabLayout tab)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.json = json;
        this.tab = tab;

        if (fragments.size() == 0)
        {

            fragments.add(0, new MainFragment(currLocJson));
            for (int i = 1; i < json.size() + 1; ++i)
            {
                fragments.add(i, DynamicFragment.newInstance(i, json.get(i - 1)));
            }
        }
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object)
    {
        if (removing) {
            return PagerAdapter.POSITION_NONE;
        }
        else{
            return PagerAdapter.POSITION_UNCHANGED;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }


    public void removeFragment(Fragment fragment, int position)
    {
        fragments.remove(position);
        removing = true;
        --mNumOfTabs;
        tab.removeTabAt(position);
        notifyDataSetChanged();
        removing = false;

    }


}


