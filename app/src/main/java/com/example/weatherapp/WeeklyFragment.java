package com.example.weatherapp;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
//weekly tab fragment for details activity
public class WeeklyFragment extends Fragment {

    private final String TAG = "WEEKLY_FRAGMENT";


    public WeeklyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weekly, container, false);;

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
            JSONObject daily = forecast.getJSONObject("daily");
            String summary = daily.getString("summary");
            String icon = daily.getString("icon");

            ImageView iconView = view.findViewById(R.id.iconView);

            if (icon.equals("clear-day"))
            {
                iconView.setImageResource(R.drawable.ic_weather_sunny_white_24dp);
                iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.orange));
            }
            if (icon.equals("clear-night"))
            {
                iconView.setImageResource(R.drawable.ic_weather_night_white_24dp);
            }

            if (icon.equals("rain"))
            {
                iconView.setImageResource(R.drawable.ic_weather_rainy_white_24dp);
            }
            if (icon.equals("sleet"))
            {
                iconView.setImageResource(R.drawable.ic_weather_snowy_rainy_white_24dp);
            }
            if (icon.equals("wind"))
            {
                iconView.setImageResource(R.drawable.ic_weather_windy_variant_white_24dp);
            }
            if (icon.equals("snow"))
            {
                iconView.setImageResource(R.drawable.ic_weather_snowy_white_24dp);
            }
            if (icon.equals("fog"))
            {
                iconView.setImageResource(R.drawable.ic_weather_fog_white_24dp);
            }
            if (icon.equals("cloudy"))
            {
                iconView.setImageResource(R.drawable.ic_weather_cloudy_white_24dp);
            }
            if (icon.equals("partly-cloudy-night"))
            {
                iconView.setImageResource(R.drawable.ic_weather_night_partly_cloudy_white_24dp);
            }
            if (icon.equals("partly-cloudy-day"))
            {
                iconView.setImageResource(R.drawable.ic_weather_partly_cloudy_white_24dp);
            }

            TextView summaryView = view.findViewById(R.id.summaryView);
            summaryView.setText(summary);

            LineChart chart = (LineChart) view.findViewById(R.id.chart);
            List<Entry> tempLowEntries = new ArrayList<Entry>();
            List<Entry> tempHighEntries = new ArrayList<Entry>();

            JSONArray data = daily.getJSONArray("data");

            for (int i = 0; i < data.length(); ++i)
            {
                JSONObject dataObject = data.getJSONObject(i);
                float tempLow = (float) dataObject.getDouble("temperatureLow");
                float tempHigh = (float) dataObject.getDouble("temperatureHigh");
                tempLowEntries.add(new Entry(i, tempLow));
                tempHighEntries.add(new Entry(i, tempHigh));
            }

            LineDataSet tempLowDataSet = new LineDataSet(tempLowEntries, "Minimum Temperature");
            LineDataSet tempHighDataSet = new LineDataSet(tempHighEntries, "Maximum Temperature");
            tempLowDataSet.setColors(new int[] { R.color.purple }, getContext());
            tempHighDataSet.setColors(new int[] { R.color.orange }, getContext());

            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            lineDataSets.add(tempLowDataSet);
            lineDataSets.add(tempHighDataSet);

            LineData lineData = new LineData(lineDataSets);

            chart.setData(lineData);
            chart.setScaleEnabled(true);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            xAxis.setDrawGridLines(false);

            YAxis yAxisRight = chart.getAxisRight();
            YAxis yAxisLeft = chart.getAxisLeft();
            xAxis.setTextColor(getResources().getColor(R.color.white));
            yAxisRight.setTextColor(getResources().getColor(R.color.white));
            yAxisLeft.setTextColor(getResources().getColor(R.color.white));

            Legend legend = chart.getLegend();
            legend.setEnabled(true);
            legend.setTextColor(getResources().getColor(R.color.white));
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setTextSize(14);
            chart.invalidate();

        }

        catch (Exception e)
        {
            Log.i(TAG, "Weekly Fragment error");
        }
    }

}
