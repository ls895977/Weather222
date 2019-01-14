package com.naran.Utils;

import android.os.Environment;

/**
 * Created by darhandarhad on 2017/8/8.
 */

public class SdCardUtil {

    private static SdCardUtil instance;
    private SdCardUtil(){}
    public static SdCardUtil getInstance(){

        if(null==instance){
            instance = new SdCardUtil();
        }
        return instance;
    }
    public String getSdCardPath(){

        String sdCardPath = "";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if(sdCardExist){

            sdCardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        }else{
            sdCardPath = Environment.getDataDirectory().getAbsolutePath();
        }
        return sdCardPath;
    }
}
