package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


//dynamic fragment for favorited locations on the main activity
public class DynamicFragment extends Fragment {
    View view;
    private String weather;
    private String city;
    private String state;
    private String country;
    private final String TAG = "DYNAMIC_FRAGMENT";

    public DynamicFragment(String json)
    {
        weather = json;
    }

    public static DynamicFragment newInstance(int val, String json) {
        DynamicFragment fragment = new DynamicFragment(json);
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        return fragment;
    }

    //inflate and render the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_dynamic, container, false);

        renderView(view);
        setFavoritesButton(view);

        CardView card = view.findViewById(R.id.cardView1);
        card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchDetails(view);
            }
        });

        return view;
    }

    //launch details activity on click
    public void launchDetails(View view)
    {

        Intent intent = new Intent(getActivity(), ProgressBarActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("country", country);
        startActivity(intent);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("json", weather.toString() );
                intent.putExtra("city", city);
                intent.putExtra("state", state);
                intent.putExtra("country", country);
                startActivity(intent);
            }
        }, 3500);


    }

    //listen and remove favorites on click
    private void setFavoritesButton(View view)
    {
        final FloatingActionButton favoriteButton = view.findViewById(R.id.favoriteButton);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.isEmpty()) {
                    Toast.makeText(getContext(), city + ", " + country + " was removed from favorites",
                            Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(getContext(), city + ", " + state + ", " + country + " was removed from favorites",
                            Toast.LENGTH_LONG).show();
                }

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPrefs.edit();
                final String favoritesString = sharedPrefs.getString("favorites", null);
                final Gson gson = new Gson();

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

                }

                MainActivity mainActivity = (MainActivity) getActivity();
                PlansPagerAdapter plansPagerAdapter = mainActivity.getPagerAdapter();
                ViewPager viewPager = mainActivity.getViewPager();
                int position = viewPager.getCurrentItem();
                plansPagerAdapter.removeFragment(new Fragment(), position);

            }

        });
    }

    //renders the view with real-time data
    private void renderView(View view)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(weather);
            city = jsonObject.getString("city");
            state = jsonObject.getString("state");
            country = jsonObject.getString("country");

            JSONObject forecast = jsonObject.getJSONObject("forecast");
            JSONObject currently = forecast.getJSONObject("currently");
            int temp = currently.getInt("temperature");
            String summary = currently.getString("summary");
            String icon = currently.getString("icon");

            TextView addressView = view.findViewById(R.id.addressView);
            if (state.isEmpty())
            {
                addressView.setText(city + ", " + country);
            }
            else
            {
                addressView.setText(city + ", " + state + ", " + country);
            }

            TextView summaryView = view.findViewById(R.id.summaryView);
            summaryView.setText(summary);
            TextView tempView = view.findViewById(R.id.tempView);
            tempView.setText(temp + "°F");
            ImageView iconView = view.findViewById(R.id.iconView);

            if (icon.equals("clear-day"))
            {
                iconView.setImageResource(R.drawable.ic_weather_sunny_white_24dp);
                iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.orange));
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
            String humidity = df2.format(currently.getDouble("humidity") * 100);
            String windSpeed = df.format(currently.getDouble("windSpeed"));
            String visibility = df.format(currently.getDouble("visibility"));
            String pressure = df.format(currently.getDouble("pressure"));

            TextView humidityTextView = view.findViewById(R.id.humidityTextView);
            humidityTextView.setText(humidity + "%");

            TextView windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
            windSpeedTextView.setText(windSpeed + " mph");

            TextView visibilityTextView = view.findViewById(R.id.visibilityTextView);
            visibilityTextView.setText(visibility + " km");

            TextView pressureTextView = view.findViewById(R.id.pressureTextView);
            pressureTextView.setText(pressure + " mb");

            ArrayList<WeatherPrediction> weatherPredictions = new ArrayList<>();

            JSONObject daily = forecast.getJSONObject("daily");
            JSONArray dataArr = daily.getJSONArray("data");
            for (int i = 0; i < 8; ++i) {

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

            ListView listView = view.findViewById(R.id.listView);
            WeatherPredictionsAdaptor adaptor = new WeatherPredictionsAdaptor(getContext(), R.layout.card3_row_layout, weatherPredictions);
            listView.setAdapter(adaptor);
        }

        catch (Exception e)
        {
            Log.i(TAG, "Error rendering dynamic fragment");
        }

    }

}
