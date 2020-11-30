package com.example.weatherapp;

//abstraction for weather prediction row
public class WeatherPrediction {

    private String date;
    private String icon;
    private Integer minTemp;
    private Integer maxTemp;

    public WeatherPrediction(String date, String icon, Integer minTemp, Integer maxTemp)
    {
        this.date = date;
        this.icon = icon;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getIcon()
    {
        return this.icon;
    }

    public Integer getMinTemp()
    {
        return this.minTemp;
    }

    public Integer getMaxTemp()
    {
        return this.maxTemp;
    }
}
