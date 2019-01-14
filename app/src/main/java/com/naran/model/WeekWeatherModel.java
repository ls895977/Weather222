package com.naran.model;

import org.json.JSONObject;

/**
 * Created by MENK021 on 2017/1/11.
 */

public class WeekWeatherModel {
    private String ForecastDate;
    private String AreaNO;
    private int HighTemperature;
    private int LowTemperature;
    private int WindSpeed;
    private String WeatherPhenomenon;
    private int WeatherPhenomenonID;
    private int WindSpeedMS;
    private String WindDirection;

    public WeekWeatherModel(JSONObject jsonObject){

        this.ForecastDate = jsonObject.optString("ForecastDate");
        this.AreaNO = jsonObject.optString("AreaNO");
        this.HighTemperature = jsonObject.optInt("HighTemperature");
        this.LowTemperature = jsonObject.optInt("LowTemperature");
        this.WindSpeed = jsonObject.optInt("WindSpeed");
        this.WeatherPhenomenon = jsonObject.optString("WeatherPhenomenon");
        this.WeatherPhenomenonID = jsonObject.optInt("WeatherPhenomenonID");
        this.WindSpeedMS = jsonObject.optInt("WindSpeedMS");
        this.WindDirection = jsonObject.optString("WindDirection");
    }
    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }
    public int getWeatherPhenomenonID() {
        return WeatherPhenomenonID;
    }

    public void setWeatherPhenomenonID(int weatherPhenomenonID) {
        WeatherPhenomenonID = weatherPhenomenonID;
    }
    public String getForecastDate() {
        return ForecastDate;
    }

    public void setForecastDate(String forecastDate) {
        ForecastDate = forecastDate;
    }

    public String getAreaNO() {
        return AreaNO;
    }

    public void setAreaNO(String areaNO) {
        AreaNO = areaNO;
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

    public int getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return WeatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        WeatherPhenomenon = weatherPhenomenon;
    }

    public int getWindSpeedMS() {
        return WindSpeedMS;
    }

    public void setWindSpeedMS(int windSpeedMS) {
        WindSpeedMS = windSpeedMS;
    }
}
