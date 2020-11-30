package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
//current location main fragment for main activity
public class MainFragment extends Fragment {

    private String city;
    private String state;
    private String country;
    private JSONObject currLocJson;
    private final String TAG = "MAIN_FRAGMENT";


    public MainFragment(JSONObject currLocJson)
    {
        this.currLocJson = currLocJson;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        renderView(view);

        CardView card = view.findViewById(R.id.cardView1);

        card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchDetails(view);
            }
        });

        return view;
    }

    //fetches real-time data for details activity and then launches details activity
    private void detailsRequest(String lat, String lon)
    {
        String url = "https://android-nodejs.appspot.com/weather?street=&city=&state=&lat=" + lat + "&lng=" + lon;
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            System.out.println(response);
                            Log.i("CUSTOM SEARCH",  response.getJSONObject("customSearch").toString());

                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
                            intent.putExtra("json", response.toString());
                            intent.putExtra("city", city);
                            intent.putExtra("state", state);
                            intent.putExtra("country", country);
                            startActivity(intent);

                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        queue.add(jsonObjectRequest);
    }


    public void launchDetails(View view)
    {
        Intent intent = new Intent(getActivity(), ProgressBarActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("country", country);
        startActivity(intent);

        String url = "http://ip-api.com/json";
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            System.out.println(response);
                            city = response.getString("city");
                            state = response.getString("region");
                            country = response.getString("countryCode");
                            detailsRequest(response.getString("lat"), response.getString("lon"));

                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        queue.add(jsonObjectRequest);

    }

    //renders the view
    private void renderView(View view)
    {
        try {

            city = currLocJson.getString("city");
            state = currLocJson.getString("state");
            country = currLocJson.getString("country");
            TextView addressView = view.findViewById(R.id.addressView);
            addressView.setText(city + ", " + state + ", " + country);
            JSONObject forecast = currLocJson.getJSONObject("forecast");

            JSONObject currently = forecast.getJSONObject("currently");
            int temp = currently.getInt("temperature");
            String summary = currently.getString("summary");
            String icon = currently.getString("icon");


            TextView summaryView = view.findViewById(R.id.summaryView);
            summaryView.setText(summary);
            TextView tempView = view.findViewById(R.id.tempView);
            tempView.setText(temp + "°F");
            ImageView iconView = view.findViewById(R.id.iconView);

            if (icon.equals("clear-day")) {
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
            Log.i(TAG, "Error creating view");
        }

    }

}
