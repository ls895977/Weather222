package com.naran.model;

import org.json.JSONObject;

/**
 * Created by MENK021 on 2017/1/26.
 */

public class RealTimeWeatherModel {
    private String AreaNO;
    private String ForecastDate;
    private String HighTemperature;
    private String LowTemperature;
    private String CurrentTemperature;
    private String WindDirection;
    private String WindSpeed;
    private String WeatherPhenomenon;
    private int WeatherPhenomenonID;
    private String CWColumn2;
    private String ForecastTime;
    private String PPColumn1;
    private String REColumn1;
    private String WIColumn1;
    private String WIColumn2;
    private String VVColumn1;
    private String THColumn10;
    private int new_index;
    private String FireWarning;
    private float SLatitude;
    private float SLongitude;
    private int NLatitude;
    private float NLongitude;
    private float Latitude;
    private float Longitude;
    private String CHineseAreaName;
    private int Edition;
    private int WarningAndServiceID;
    private String Title;
    private int ImgNumber;

    public String getAreaNO() {
        return AreaNO;
    }

    public void setAreaNO(String areaNO) {
        AreaNO = areaNO;
    }

    public String getForecastDate() {
        return ForecastDate;
    }

    public void setForecastDate(String forecastDate) {
        ForecastDate = forecastDate;
    }

    public String getHighTemperature() {
        return HighTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        HighTemperature = highTemperature;
    }

    public String getLowTemperature() {
        return LowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        LowTemperature = lowTemperature;
    }

    public String getCurrentTemperature() {
        return CurrentTemperature;
    }

    public void setCurrentTemperature(String currentTemperature) {
        CurrentTemperature = currentTemperature;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return WeatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        WeatherPhenomenon = weatherPhenomenon;
    }

    public int getWeatherPhenomenonID() {
        return WeatherPhenomenonID;
    }

    public void setWeatherPhenomenonID(int weatherPhenomenonID) {
        WeatherPhenomenonID = weatherPhenomenonID;
    }

    public String getCWColumn2() {
        return CWColumn2;
    }

    public void setCWColumn2(String CWColumn2) {
        this.CWColumn2 = CWColumn2;
    }

    public String getForecastTime() {
        return ForecastTime;
    }

    public void setForecastTime(String forecastTime) {
        ForecastTime = forecastTime;
    }

    public String getPPColumn1() {
        return PPColumn1;
    }

    public void setPPColumn1(String PPColumn1) {
        this.PPColumn1 = PPColumn1;
    }

    public String getREColumn1() {
        return REColumn1;
    }

    public void setREColumn1(String REColumn1) {
        this.REColumn1 = REColumn1;
    }

    public String getWIColumn1() {
        return WIColumn1;
    }

    public void setWIColumn1(String WIColumn1) {
        this.WIColumn1 = WIColumn1;
    }

    public String getWIColumn2() {
        return WIColumn2;
    }

    public void setWIColumn2(String WIColumn2) {
        this.WIColumn2 = WIColumn2;
    }

    public String getVVColumn1() {
        return VVColumn1;
    }

    public void setVVColumn1(String VVColumn1) {
        this.VVColumn1 = VVColumn1;
    }

    public String getTHColumn10() {
        return THColumn10;
    }

    public void setTHColumn10(String THColumn10) {
        this.THColumn10 = THColumn10;
    }

    public int getNew_index() {
        return new_index;
    }

    public void setNew_index(int new_index) {
        this.new_index = new_index;
    }

    public String getFireWarning() {
        return FireWarning;
    }

    public void setFireWarning(String fireWarning) {
        FireWarning = fireWarning;
    }

    public float getSLatitude() {
        return SLatitude;
    }

    public void setSLatitude(float SLatitude) {
        this.SLatitude = SLatitude;
    }

    public float getSLongitude() {
        return SLongitude;
    }

    public void setSLongitude(float SLongitude) {
        this.SLongitude = SLongitude;
    }

    public int getNLatitude() {
        return NLatitude;
    }

    public void setNLatitude(int NLatitude) {
        this.NLatitude = NLatitude;
    }

    public float getNLongitude() {
        return NLongitude;
    }

    public void setNLongitude(float NLongitude) {
        this.NLongitude = NLongitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public String getCHineseAreaName() {
        return CHineseAreaName;
    }

    public void setCHineseAreaName(String CHineseAreaName) {
        this.CHineseAreaName = CHineseAreaName;
    }

    public int getEdition() {
        return Edition;
    }

    public void setEdition(int edition) {
        Edition = edition;
    }

    public int getWarningAndServiceID() {
        return WarningAndServiceID;
    }

    public void setWarningAndServiceID(int warningAndServiceID) {
        WarningAndServiceID = warningAndServiceID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImgNumber() {
        return ImgNumber;
    }

    public void setImgNumber(int imgNumber) {
        ImgNumber = imgNumber;
    }

    public RealTimeWeatherModel(JSONObject jsonObject) {
        this.AreaNO = jsonObject.optString("AreaNO");
        this.ForecastDate = jsonObject.optString("ForecastDate");
        this.HighTemperature = jsonObject.optString("HighTemperature");
        this.LowTemperature = jsonObject.optString("LowTemperature");
        this.CurrentTemperature = jsonObject.optString("CurrentTemperature");
        this.WindDirection = jsonObject.optString("WindDirection");
        this.WindSpeed = jsonObject.optString("WindSpeed");
        this.WeatherPhenomenon = jsonObject.optString("WeatherPhenomenon");
        this.WeatherPhenomenonID = jsonObject.optInt("WeatherPhenomenonID");
        this.CWColumn2 = jsonObject.optString("CWColumn2");
        this.ForecastTime = jsonObject.optString("ForecastTime");
        this.PPColumn1 = jsonObject.optString("PPColumn1");
        this.REColumn1 = jsonObject.optString("REColumn1");
        this.WIColumn1 = jsonObject.optString("WIColumn1");
        this.WIColumn2 = jsonObject.optString("WIColumn2");
        this.VVColumn1 = jsonObject.optString("VVColumn1");
        this.THColumn10 = jsonObject.optString("THColumn10");
        this.FireWarning = jsonObject.optString("FireWarning");
        this.SLatitude = jsonObject.optInt("SLatitude");
        this.SLongitude = jsonObject.optInt("SLongitude");
        this.NLatitude = jsonObject.optInt("NLatitude");
        this.NLongitude = jsonObject.optInt("NLongitude");
        this.Latitude = jsonObject.optInt("Latitude");
        this.Longitude = jsonObject.optInt("Longitude");
        this.CHineseAreaName = jsonObject.optString("CHineseAreaName");
        this.Edition = jsonObject.optInt("Edition");
        this.WarningAndServiceID = jsonObject.optInt("WarningAndServiceID");
        this.Title = jsonObject.optString("Title");
        this.ImgNumber = jsonObject.optInt("ImgNumber");
    }
}
