package com.example.weatherapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

//adapter for weather predictions listview
public class WeatherPredictionsAdaptor extends ArrayAdapter<WeatherPrediction> {

    private Context context;
    private int resource;

    public WeatherPredictionsAdaptor(Context context, int resource, ArrayList<WeatherPrediction> weatherPredictions)
    {
        super(context, resource, weatherPredictions );
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String date = getItem(position).getDate();
        String icon = getItem(position).getIcon();
        Integer minTemp = getItem(position).getMinTemp();
        Integer maxTemp = getItem(position).getMaxTemp();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView dateView = convertView.findViewById(R.id.dateView);
        ImageView iconView = convertView.findViewById(R.id.iconView);
        TextView minView = convertView.findViewById(R.id.minView);
        TextView maxView = convertView.findViewById(R.id.maxView);

        dateView.setText(date.toString());
        minView.setText(minTemp.toString());
        maxView.setText(maxTemp.toString());

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

        return convertView;



    }




}
