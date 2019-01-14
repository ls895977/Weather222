package com.naran.model;

/**
 * Created by darhandarhad on 2017/11/9.
 */

public class ProvinceAreaModel {
    private int CnAreaID;
    private int ParentID;
    private String CnAreaName;
    private String CnAreaNo;
    private int CnType;
    private int SortNo;

    public int getCnAreaID() {
        return CnAreaID;
    }

    public void setCnAreaID(int cnAreaID) {
        CnAreaID = cnAreaID;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public String getCnAreaName() {
        return CnAreaName;
    }

    public void setCnAreaName(String cnAreaName) {
        CnAreaName = cnAreaName;
    }

    public String getCnAreaNo() {
        return CnAreaNo;
    }

    public void setCnAreaNo(String cnAreaNo) {
        CnAreaNo = cnAreaNo;
    }

    public int getCnType() {
        return CnType;
    }

    public void setCnType(int cnType) {
        CnType = cnType;
    }

    public int getSortNo() {
        return SortNo;
    }

    public void setSortNo(int sortNo) {
        SortNo = sortNo;
    }
}
