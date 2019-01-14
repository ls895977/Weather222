package com.naran.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by MENK021 on 2016/12/26.
 */

public class RealTimeWeather extends LinearLayout {
    private Context context;

    private HorizontalScrollView horizontalScrollView;
    public RealTimeWeather(Context context) {

        super(context);
        this.context = context;
        init();
    }

    public RealTimeWeather(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public RealTimeWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    private void init(){

        // 左右滾動的東西
        horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1));
        this.addView(horizontalScrollView);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalScrollView.addView(linearLayout);

        DrawTimeWeatherView drawTimeWeatherView = new DrawTimeWeatherView(context);
        linearLayout.addView(drawTimeWeatherView);
    }
}
