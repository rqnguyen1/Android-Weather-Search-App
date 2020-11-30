package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;



//activity for weather details page
public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem todayTab, weeklyTab, photosTab;
    public PagerAdapter pagerAdapter;
    private String city;
    private String state;
    private String country;
    private JSONObject json;

    private final String TAG = "DETAILS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tabLayout = findViewById(R.id.tabLayout);
        todayTab = findViewById(R.id.todayTab);
        weeklyTab = findViewById(R.id.weeklyTab);
        photosTab = findViewById(R.id.photosTab);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_calendar_today_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_trending_up_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_google_photos_white_24dp);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {


            }

        });

        Intent intent = getIntent();
        try
        {
            String jsonString = intent.getStringExtra("json");
            json = new JSONObject(jsonString);

        }

        catch (Exception e)
        {
            Log.i(TAG, "Error converting JSON string" );
        }

        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");
        country = intent.getStringExtra("country");

        if (state.isEmpty())
        {
            getSupportActionBar().setTitle(city + ", " + country);
        }
        else {
            getSupportActionBar().setTitle(city + ", " + state + ", " + country);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //inflates twitter button on menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.ic_twitter, menu);
        return true;
    }

    //twitter button functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.twitter:
                try
                {
                    Double temperature = json.getJSONObject("forecast").getJSONObject("currently").getDouble("temperature");
                    Intent intent;
                    if (state.isEmpty()) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check Out " + city + ", " + country + "\'s Weather! It is " + Math.round(temperature) + "°F!&hashtags=CSCI571WeatherSearch"));
                    }
                    else
                    {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check Out " + city + ", " + state + ", " + country + "\'s Weather! It is " + Math.round(temperature) + "°F!&hashtags=CSCI571WeatherSearch"));
                    }
                    startActivity(intent);
                    return true;
                }

                catch ( Exception e)
                {
                    Log.i(TAG, "Error retrieving temperature from JSON");
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //retrieve json passed from main activity
    public String getJson()
    {
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        return json;
    }

    //back button functionality
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }


}
