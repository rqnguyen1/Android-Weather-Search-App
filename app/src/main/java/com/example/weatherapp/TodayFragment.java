package com.example.weatherapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import org.json.JSONObject;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
//today tab fragment for details activity
public class TodayFragment extends Fragment {

    private final String TAG = "TODAY_FRAGMENT";

    public TodayFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);

        renderView(view);

        return view;
    }

    //renders the view
    private void renderView(View view)
    {
        DetailsActivity detailsActivity = (DetailsActivity) getActivity();
        String json = detailsActivity.getJson();
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONObject forecast = jsonObject.getJSONObject("forecast");
            JSONObject currently = forecast.getJSONObject("currently");

            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormat df2 = new DecimalFormat("0");
            String humidity = df2.format(currently.getDouble("humidity") * 100);
            String windSpeed = df.format(currently.getDouble("windSpeed"));
            String visibility = df.format(currently.getDouble("visibility"));
            String pressure = df.format(currently.getDouble("pressure"));
            String precipIntensity = df.format(currently.getDouble("precipIntensity"));
            String temp = df2.format(currently.getDouble("temperature"));
            String cloudCover = df2.format(currently.getDouble("cloudCover") * 100);
            String ozone = df.format(currently.getDouble("ozone"));
            String icon = currently.getString("icon");

            TextView windSpeedView = view.findViewById(R.id.windSpeedView);
            windSpeedView.setText(windSpeed + " mph");

            TextView pressureView= view.findViewById(R.id.pressureView);
            pressureView.setText(pressure + " mb");

            TextView precipView = view.findViewById(R.id.precipView);
            precipView.setText(precipIntensity+ " mmph");

            TextView tempView = view.findViewById(R.id.tempView);
            tempView.setText(temp+ " °F");

            TextView humidityView = view.findViewById(R.id.humidityView);
            humidityView.setText(humidity+"%" );

            TextView visibilityView = view.findViewById(R.id.visibilityView);
            visibilityView.setText(visibility + " km");

            TextView cloudCoverView = view.findViewById(R.id.cloudCoverView);
            cloudCoverView.setText(cloudCover + "%" );

            TextView ozoneView = view.findViewById(R.id.ozoneView);
            ozoneView.setText(ozone + " DU" );

            ImageView iconView = view.findViewById(R.id.iconView);
            TextView iconTextView = view.findViewById(R.id.iconTextView);

            if (icon.equals("clear-day"))
            {
                iconView.setImageResource(R.drawable.ic_weather_sunny_white_24dp);
                iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.orange));
                iconTextView.setText("clear day");
            }
            if (icon.equals("clear-night"))
            {
                iconView.setImageResource(R.drawable.ic_weather_night_white_24dp);
                iconTextView.setText("clear night");
            }

            if (icon.equals("rain"))
            {
                iconView.setImageResource(R.drawable.ic_weather_rainy_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("sleet"))
            {
                iconView.setImageResource(R.drawable.ic_weather_snowy_rainy_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("wind"))
            {
                iconView.setImageResource(R.drawable.ic_weather_windy_variant_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("snow"))
            {
                iconView.setImageResource(R.drawable.ic_weather_snowy_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("fog"))
            {
                iconView.setImageResource(R.drawable.ic_weather_fog_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("cloudy"))
            {
                iconView.setImageResource(R.drawable.ic_weather_cloudy_white_24dp);
                iconTextView.setText(icon);
            }
            if (icon.equals("partly-cloudy-night"))
            {
                iconView.setImageResource(R.drawable.ic_weather_night_partly_cloudy_white_24dp);
                iconTextView.setText("cloudy night");
            }
            if (icon.equals("partly-cloudy-day"))
            {
                iconView.setImageResource(R.drawable.ic_weather_partly_cloudy_white_24dp);
                iconTextView.setText("cloudy day");
            }


        }

        catch (Exception e)
        {
            Log.i(TAG, "ERROR creating view");
        }

    }


}
