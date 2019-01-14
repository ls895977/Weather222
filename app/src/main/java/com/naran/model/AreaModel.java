package com.naran.model;

import org.json.JSONObject;

/**
 * Created by MENK021 on 2017/1/11.
 */

public class AreaModel {

    private int FireWarning;
    private String AreaNO;
    private String MongolianAreaName;
    private String CHineseAreaName;
    private int AreaID;

    public AreaModel(JSONObject jb) {
        this.AreaID = jb.optInt("AreaID");
        this.AreaNO = jb.optString("AreaNO");
        this.CHineseAreaName = jb.optString("CHineseAreaName");
        this.FireWarning = jb.optInt("FireWarning");
        this.MongolianAreaName = jb.optString("MongolianAreaName");

    }

    public int getFireWarning() {
        return FireWarning;
    }

    public void setFireWarning(int fireWarning) {
        FireWarning = fireWarning;
    }

    public String getAreaNO() {
        return AreaNO;
    }

    public void setAreaNO(String areaNO) {
        AreaNO = areaNO;
    }

    public String getMongolianAreaName() {
        return MongolianAreaName;
    }

    public void setMongolianAreaName(String mongolianAreaName) {
        MongolianAreaName = mongolianAreaName;
    }

    public String getCHineseAreaName() {
        return CHineseAreaName;
    }

    public void setCHineseAreaName(String CHineseAreaName) {
        this.CHineseAreaName = CHineseAreaName;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
    }
}
