package com.naran.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by surleg on 2017/3/15.
 */

public class WarnListInfoModel {
    private String WarningTitleMongolian;
    private String WarningAndServiceID;
    private String ImgNumber;
    private String ContentMongolian;
    private String DepartmentMongolia;
    private String WarningTitleCHinese;
    private String AddTimeCHinese;
    private String AddTimeMongolian;
    private String ContentCHinese;
    private String Department;

    public WarnListInfoModel(JSONObject object) {
        try {
            this.WarningTitleMongolian = object.getString("WarningTitleMongolian");
            this.WarningAndServiceID = object.getString("WarningAndServiceID");
            this.ImgNumber = object.getString("ImgNumber");
            this.ContentMongolian = object.getString("ContentMongolian");
            this.DepartmentMongolia = object.getString("DepartmentMongolia");
            this.WarningTitleCHinese = object.getString("WarningTitleCHinese");
            this.AddTimeCHinese = object.getString("AddTimeCHinese");
            this.AddTimeMongolian = object.getString("AddTimeMongolian");
            this.ContentCHinese = object.getString("ContentCHinese");
            this.Department = object.getString("Department");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWarningTitleMongolian() {
        return WarningTitleMongolian;
    }

    public void setWarningTitleMongolian(String warningTitleMongolian) {
        WarningTitleMongolian = warningTitleMongolian;
    }

    public String getWarningAndServiceID() {
        return WarningAndServiceID;
    }

    public void setWarningAndServiceID(String warningAndServiceID) {
        WarningAndServiceID = warningAndServiceID;
    }

    public String getImgNumber() {
        return ImgNumber;
    }

    public void setImgNumber(String imgNumber) {
        ImgNumber = imgNumber;
    }

    public String getContentMongolian() {
        return ContentMongolian;
    }

    public void setContentMongolian(String contentMongolian) {
        ContentMongolian = contentMongolian;
    }

    public String getDepartmentMongolia() {
        return DepartmentMongolia;
    }

    public void setDepartmentMongolia(String departmentMongolia) {
        DepartmentMongolia = departmentMongolia;
    }

    public String getWarningTitleCHinese() {
        return WarningTitleCHinese;
    }

    public void setWarningTitleCHinese(String warningTitleCHinese) {
        WarningTitleCHinese = warningTitleCHinese;
    }

    public String getAddTimeCHinese() {
        return AddTimeCHinese;
    }

    public void setAddTimeCHinese(String addTimeCHinese) {
        AddTimeCHinese = addTimeCHinese;
    }

    public String getAddTimeMongolian() {
        return AddTimeMongolian;
    }

    public void setAddTimeMongolian(String addTimeMongolian) {
        AddTimeMongolian = addTimeMongolian;
    }

    public String getContentCHinese() {
        return ContentCHinese;
    }

    public void setContentCHinese(String contentCHinese) {
        ContentCHinese = contentCHinese;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
};
