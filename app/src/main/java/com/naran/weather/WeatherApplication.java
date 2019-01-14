package com.naran.weather;

import android.app.Application;

/**
 * Created by MENK021 on 2017/1/18.
 */

public class WeatherApplication extends Application {
    /** TAG */
    public static final String TAG = "CrashApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler.getInstance().init(this);
    }
}
