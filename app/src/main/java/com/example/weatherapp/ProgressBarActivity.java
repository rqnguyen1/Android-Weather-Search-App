package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import android.content.Intent;

//progress bar activity
public class ProgressBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String state = intent.getStringExtra("state");
        String country = intent.getStringExtra("country");

        if (state.isEmpty())
            getSupportActionBar().setTitle(city + ", " + country);
        else
            getSupportActionBar().setTitle(city + ", " + state + ", " + country);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
            }
        }, 6000);

    }

}
