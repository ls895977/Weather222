package com.naran.model;

import org.json.JSONObject;

/**
 * Created by MENK021 on 2017/1/12.
 */

public class UserInfoModel {


    private int RoleID;
    private String UserName;
    private int ID;
    private String FireWarning;
    private int ParentID ;
    private String FullName;
    private String CHineseAreaName;
    private String HeadImg;
    private String AreaNO;
    private String Contact;
    private int Edition;
    private String ParentUserName;
    private int State;

    public UserInfoModel(JSONObject jsonObject){
        this.AreaNO = jsonObject.optString("AreaNO");
       this.CHineseAreaName = jsonObject.optString("CHineseAreaName");
       this.Contact = jsonObject.optString("Contact");
       this.Edition = jsonObject.optInt("Edition");
      this.FireWarning  = jsonObject.optString("FireWarning");
       this.FullName = jsonObject.optString("FullName");
       this.HeadImg = jsonObject.optString("HeadImg");
      this.ID  = jsonObject.optInt("ID");
      this.ParentID  = jsonObject.optInt("ParentID");
      this.UserName  = jsonObject.optString("");
      this.RoleID  = jsonObject.optInt("RoleID");
      this.ParentUserName  = jsonObject.optString("ParentUserName");
      this.State  = jsonObject.optInt("State");
    }
    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFireWarning() {
        return FireWarning;
    }

    public void setFireWarning(String fireWarning) {
        FireWarning = fireWarning;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getCHineseAreaName() {
        return CHineseAreaName;
    }

    public void setCHineseAreaName(String CHineseAreaName) {
        this.CHineseAreaName = CHineseAreaName;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getAreaNO() {
        return AreaNO;
    }

    public void setAreaNO(String areaNO) {
        AreaNO = areaNO;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public int getEdition() {
        return Edition;
    }

    public void setEdition(int edition) {
        Edition = edition;
    }

    public String getParentUserName() {
        return ParentUserName;
    }

    public void setParentUserName(String parentUserName) {
        ParentUserName = parentUserName;
    }

}
