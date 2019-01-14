package com.naran.model;

import org.json.JSONObject;

/**
 * Created by darhandarhad on 2017/7/8.
 */

public class WranAndServiceModel {


    private String Title;
    private String AddTime;
    private String ImgNumber;
    private String WarningAndServiceID;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getImgNumber() {
        return ImgNumber;
    }

    public void setImgNumber(String imgNumber) {
        ImgNumber = imgNumber;
    }

    public String getWarningAndServiceID() {
        return WarningAndServiceID;
    }

    public void setWarningAndServiceID(String warningAndServiceID) {
        WarningAndServiceID = warningAndServiceID;
    }
    public WranAndServiceModel(JSONObject jb){
        if(jb!=null) {
            this.AddTime = jb.optString("AddTime");
            this.ImgNumber = jb.optString("ImgNumber");
            this.Title = jb.optString("Title");
            this.WarningAndServiceID = jb.optString("WarningAndServiceID");
        }
    }
    //meng gu wen
    public WranAndServiceModel(JSONObject jb,int i){
        if(jb!=null) {
            this.AddTime = jb.optString("AddTime");
            this.ImgNumber = jb.optString("ImgNumber");
            String titleString = jb.optString("MongolianTitle");
//            int titleLenght = titleString.length();
//            if( titleLenght<55){
//
//                for(int j=0;j<55-titleLenght;j++){
//                    titleString = titleString+" ";
//                }
//            }else{
//                titleString = titleString.substring(0,54);
//            }
            this.Title = titleString;
            this.WarningAndServiceID = jb.optString("WarningAndServiceID");
        }
    }
}
