package com.naran.addressmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darhandarhad on 2018/12/13.
 */

public class OnAddressClickTask {

    List<OnAddressClickListener> onAddressClickListeners = new ArrayList<>();

    private OnAddressClickTask() {
    }

    private static OnAddressClickTask instance;

    public static OnAddressClickTask getInstance() {

        if (instance == null) {
            instance = new OnAddressClickTask();
        }
        return instance;
    }

    public void addListener(OnAddressClickListener musicProgressListener) {
        if (onAddressClickListeners != null) {
            onAddressClickListeners.add(musicProgressListener);
        }
    }

    public void removeListener(OnAddressClickListener musicProgressListener) {
        if (onAddressClickListeners.contains(musicProgressListener)) {
            onAddressClickListeners.remove(musicProgressListener);
        }
    }

    public void fireMsg(int tag,TextArticleTitle textArticleTitle) {
        if(null!=onAddressClickListeners)
            for (OnAddressClickListener musicProgressListener : onAddressClickListeners) {
                if(null!=musicProgressListener)
                    musicProgressListener.onClick(tag,textArticleTitle);
            }
    }
}
