package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

//activity for search queries
public class SearchableActivity extends AppCompatActivity {

    private JSONObject jsonObject;
    private String city;
    private String state;
    private String country;
    private boolean favorited = false;
    private final String TAG = "SEARCHABLE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");
        country = intent.getStringExtra("country");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (state.isEmpty())
            getSupportActionBar().setTitle(city + ", " + country);
        else
            getSupportActionBar().setTitle(city + ", " + state + ", " + country);

        TextView addressView = findViewById(R.id.addressView);
        if (state.isEmpty())
        {
            addressView.setText(city + ", " + country);
        }

        else
        {
            addressView.setText(city + ", " + state + ", " + country);
        }

        searchRequest();
        setFavoritesButton();

    }

    //back button functionality
    @Override
    public boolean onSupportNavigateUp()
    {
        Intent intent = new Intent(SearchableActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    //launches details activity
    public void launchDetails(View view)
    {

        Intent i = new Intent(SearchableActivity.this, ProgressBarActivity.class);
        i.putExtra("city", city);
        i.putExtra("state", state);
        i.putExtra("country", country);

        startActivity(i);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SearchableActivity.this, DetailsActivity.class);
                intent.putExtra("json", jsonObject.toString() );
                intent.putExtra("city", city);
                intent.putExtra("state", state);
                intent.putExtra("country", country);
                startActivity(intent);
            }
        }, 3500);

    }

    //fetches real-time data based on search query
    public void searchRequest()
    {

        String url = "https://android-nodejs.appspot.com/weather?street=&city=" + city + "&state="+ state + "&country=" + country + "&lat=&lng=";
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Log.i(TAG, "onSearchResponse: " + response);
                            jsonObject = response;
                            ConstraintLayout progress = findViewById(R.id.progress);
                            progress.setVisibility(View.GONE);
                            renderView();


                        }
                        catch (Exception e)
                        {
                            Log.i(TAG, "Search request error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "Search request error");
            }
        });

        queue.add(jsonObjectRequest);
    }

    //renders the view
    private void renderView()
    {

        try
        {
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("country", country);
            JSONObject forecast = jsonObject.getJSONObject("forecast");
            JSONObject currently = forecast.getJSONObject("currently");

            int temp = currently.getInt("temperature");
            String summary = currently.getString("summary");
            String icon = currently.getString("icon");


            TextView summaryView = findViewById(R.id.summaryView);
            summaryView.setText(summary);
            TextView tempView = findViewById(R.id.tempView);
            tempView.setText(temp + "°F" );
            ImageView iconView = findViewById(R.id.iconView);

            if (icon.equals("clear-day")) {
                iconView.setImageResource(R.drawable.ic_weather_sunny_white_24dp);
                iconView.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange));
            }
            if (icon.equals("clear-night"))
                iconView.setImageResource(R.drawable.ic_weather_night_white_24dp);
            if (icon.equals("rain"))
                iconView.setImageResource(R.drawable.ic_weather_rainy_white_24dp);
            if (icon.equals("sleet"))
                iconView.setImageResource(R.drawable.ic_weather_snowy_rainy_white_24dp);
            if (icon.equals("wind"))
                iconView.setImageResource(R.drawable.ic_weather_windy_variant_white_24dp);
            if (icon.equals("snow"))
                iconView.setImageResource(R.drawable.ic_weather_snowy_white_24dp);
            if (icon.equals("fog"))
                iconView.setImageResource(R.drawable.ic_weather_fog_white_24dp);
            if (icon.equals("cloudy"))
                iconView.setImageResource(R.drawable.ic_weather_cloudy_white_24dp);
            if (icon.equals("partly-cloudy-night"))
                iconView.setImageResource(R.drawable.ic_weather_night_partly_cloudy_white_24dp);
            if (icon.equals("partly-cloudy-day"))
                iconView.setImageResource(R.drawable.ic_weather_partly_cloudy_white_24dp);

            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormat df2 = new DecimalFormat("0");
            String humidity = df2.format(currently.getDouble("humidity")*100);
            String windSpeed = df.format(currently.getDouble("windSpeed"));
            String visibility = df.format(currently.getDouble("visibility"));
            String pressure = df.format(currently.getDouble("pressure"));

            TextView humidityTextView = findViewById(R.id.humidityTextView);
            humidityTextView.setText(humidity + "%");

            TextView windSpeedTextView = findViewById(R.id.windSpeedTextView);
            windSpeedTextView.setText(windSpeed + " mph");

            TextView visibilityTextView = findViewById(R.id.visibilityTextView);
            visibilityTextView.setText(visibility + " km");

            TextView pressureTextView = findViewById(R.id.pressureTextView);
            pressureTextView.setText(pressure + " mb");

            ArrayList<WeatherPrediction> weatherPredictions = new ArrayList<>();

            JSONObject daily = forecast.getJSONObject("daily");
            JSONArray dataArr = daily.getJSONArray("data");
            for (int i = 0; i < 8; ++i)
            {

                JSONObject dataObj = dataArr.getJSONObject(i);
                String time = dataObj.getString("time");
                Date date = new Date(Long.parseLong(time) * 1000);
                String pattern = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                String formatDate = format.format(date);

                String predictionIcon = dataObj.getString("icon");
                Integer tempMax = dataObj.getInt("temperatureHigh");
                Integer tempMin = dataObj.getInt("temperatureLow");

                weatherPredictions.add(new WeatherPrediction(formatDate, predictionIcon, tempMin, tempMax));

            }

            ListView listView = findViewById(R.id.listView);
            WeatherPredictionsAdaptor adaptor = new WeatherPredictionsAdaptor(getApplicationContext(), R.layout.card3_row_layout, weatherPredictions );
            listView.setAdapter(adaptor);

        }


        catch (Exception e)
        {
            Log.i(TAG, "Error creating view");
        }

    }

    //listen to favorites button and add/remove favorites on click
    private void setFavoritesButton()
    {
        final FloatingActionButton favoriteButton = findViewById(R.id.favoriteButton);

        SharedPreferences sharedPrefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        final String favoritesString = sharedPrefs.getString("favorites", null);
        final Gson gson = new Gson();
        if (favoritesString != null)
        {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> favorites = gson.fromJson(favoritesString, type);

            try
            {
                for (int i = 0; i < favorites.size(); ++i)
                {
                    String favoriteString = favorites.get(i);
                    JSONObject favoriteJson = new JSONObject(favoriteString);

                    if (favoriteJson.getString("city").equals(city) && favoriteJson.getString("country").equals(country))
                    {
                        favoriteButton.setImageResource(R.drawable.ic_map_marker_minus_white_24dp);
                        favorited = true;
                        break;
                    }
                }
            }

            catch (Exception e)
            {
                Log.i(TAG, "Error checking favorites");
            }
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorited == false) {
                    favoriteButton.setImageResource(R.drawable.ic_map_marker_minus_white_24dp);
                    if ( favoritesString != null )
                    {
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList<String> favorites = gson.fromJson(favoritesString, type);
                        favorites.add(jsonObject.toString());
                        String favString = gson.toJson(favorites);
                        editor.putString("favorites", favString);
                        editor.apply();
                        if (state.isEmpty()) {
                            Toast.makeText(getApplicationContext(), city + ", " + country + " was added to favorites",
                                    Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), city + ", " + state + ", " + country + " was added to favorites",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    else if ( favoritesString == null)
                    {
                        ArrayList<String> favorites = new ArrayList<>();
                        favorites.add(jsonObject.toString());
                        String favString = gson.toJson(favorites);
                        editor.putString("favorites", favString);
                        editor.apply();
                        if (state.isEmpty()) {
                            Toast.makeText(getApplicationContext(), city + ", " + country + " was added to favorites",
                                    Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), city + ", " + state + ", " + country + " was added to favorites",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    favorited = true;
                }

                else
                {
                    favoriteButton.setImageResource(R.drawable.ic_map_marker_plus_white_24dp);
                    if ( favoritesString != null )
                    {
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList<String> favorites = gson.fromJson(favoritesString, type);
                        try
                        {
                            for (int i = 0; i < favorites.size(); ++i)
                            {
                                String favoriteString = favorites.get(i);
                                JSONObject favoriteJson = new JSONObject(favoriteString);
                                if (favoriteJson.getString("city").equals(city) && favoriteJson.getString("country").equals(country))
                                {
                                    favorites.remove(i);
                                    break;
                                }
                            }
                        }

                        catch (Exception e)
                        {
                            Log.i(TAG, "Error removing from favorites");
                        }

                        String favString = gson.toJson(favorites);
                        editor.putString("favorites", favString);
                        editor.apply();
                        if (state.isEmpty()) {
                            Toast.makeText(getApplicationContext(), city + ", " + country + " was removed from favorites",
                                    Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), city + ", " + state + ", " + country + " was removed from favorites",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    favorited = false;
                }
            }
        });
    }
}
