package com.naran.addressmanager;

/**
 * Created by darhandarhad on 2016/12/1.
 */

public class TextArticleTitle {

    private int id;
    private String Title;
    private String Content;
    private String areaOn;
    private String CurrentTemperature;
    private int WeatherPhenomenonID;
    private String WeatherPhenomenon;
    private String WeatherPhenomenonCn;

    public String getWeatherPhenomenon() {
        return WeatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        WeatherPhenomenon = weatherPhenomenon;
    }

    public String getWeatherPhenomenonCn() {
        return WeatherPhenomenonCn;
    }

    public void setWeatherPhenomenonCn(String weatherPhenomenonCn) {
        WeatherPhenomenonCn = weatherPhenomenonCn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentTemperature() {
        return CurrentTemperature;
    }

    public void setCurrentTemperature(String currentTemperature) {
        CurrentTemperature = currentTemperature;
    }

    public int getWeatherPhenomenonID() {
        return WeatherPhenomenonID;
    }

    public void setWeatherPhenomenonID(int weatherPhenomenonID) {
        WeatherPhenomenonID = weatherPhenomenonID;
    }

    public String getAreaOn(){
        return areaOn;
    }
    public void setAreaOn(String areaOn){
        this.areaOn = areaOn;
    }
    public int getItemID() {
        return id;
    }

    public void setItemID(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}