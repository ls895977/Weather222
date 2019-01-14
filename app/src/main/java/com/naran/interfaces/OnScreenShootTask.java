package com.naran.interfaces;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017/4/23
 * 作者：amin
 * 功能：
 */

public class OnScreenShootTask {
    private List<OnScreenShootListener> onScreenShootListeners = new ArrayList<>();
    private OnScreenShootTask(){}
    private static OnScreenShootTask instance;
    public static OnScreenShootTask getInstance(){
        if(null == instance){
            instance = new OnScreenShootTask();
        }
        return instance;
    }
    public  void addOnScreenShootListener(OnScreenShootListener ossl){
        this.onScreenShootListeners.add(ossl);
    }
    public void fireMsg(Bitmap bmp){
        for(OnScreenShootListener ossl : onScreenShootListeners){
            ossl.setOnScreenShootListener(bmp);
        }
    }
    public void removeScreenShootListener(OnScreenShootListener ossl){
            if(this.onScreenShootListeners.contains(ossl)){
                this.onScreenShootListeners.remove(ossl);
            }
    }
}
