package com.naran.model;

import org.json.JSONObject;

/**
 * Created by surleg on 2017/3/7.
 */

public class WeekWeatherModel_Chinese {
    private int HighTemperature;
    private int LowTemperature;
    private String WindDirection;
    private String AreaNO;
    private int WindSpeed;
    private int WindSpeedMS;
    private String ForecastDate;
    private String WeatherPhenomenonID;


    private String WeatherPhenomenon;



    public WeekWeatherModel_Chinese(JSONObject jsonObject) {
        this.HighTemperature = jsonObject.optInt("HighTemperature");
        this.LowTemperature = jsonObject.optInt("LowTemperature");
        this.WindDirection = jsonObject.optString("WindDirection");
        this.AreaNO = jsonObject.optString("AreaNO");
        this.WindSpeed = jsonObject.optInt("WindSpeed");
        this.WindSpeedMS = jsonObject.optInt("WindSpeedMS");
        this.ForecastDate = jsonObject.optString("ForecastDate");
        this.WeatherPhenomenonID = String.valueOf(jsonObject.optInt("WeatherPhenomenonID"));
        this.WeatherPhenomenon = jsonObject.optString("WeatherPhenomenon");
    }

    public String getWeatherPhenomenon() {
        return WeatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        WeatherPhenomenon = weatherPhenomenon;
    }
    public int getHighTemperature() {
        return HighTemperature;
    }

    public void setHighTemperature(int highTemperature) {
        HighTemperature = highTemperature;
    }

    public int getLowTemperature() {
        return LowTemperature;
    }

    public void setLowTemperature(int lowTemperature) {
        LowTemperature = lowTemperature;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public String getAreaNO() {
        return AreaNO;
    }

    public void setAreaNO(String areaNO) {
        AreaNO = areaNO;
    }

    public int getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        WindSpeed = windSpeed;
    }

    public int getWindSpeedMS() {
        return WindSpeedMS;
    }

    public void setWindSpeedMS(int windSpeedMS) {
        WindSpeedMS = windSpeedMS;
    }

    public String getForecastDate() {
        return ForecastDate;
    }

    public void setForecastDate(String forecastDate) {
        ForecastDate = forecastDate;
    }

    public String getWeatherPhenomenonID() {
        return WeatherPhenomenonID;
    }

    public void setWeatherPhenomenonID(String WeatherPhenomenonID) {
        WeatherPhenomenonID = WeatherPhenomenonID;
    }
}
