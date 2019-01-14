package com.naran.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.naran.model.WeekWeatherModel;

import java.util.List;

/**
 * Created by MENK021 on 2017/1/3.
 */

public class WeekWeather extends LinearLayout {
    private Context context;
    private DrawWeekWeatherView drawWeekWeatherView;
    private HorizontalScrollView horizontalScrollView;
    public WeekWeather(Context context) {

        super(context);
        this.context = context;
        init();
    }

    public WeekWeather(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public WeekWeather(Context context, AttributeSet attrs, int defStyleAttr) {
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
        drawWeekWeatherView = new DrawWeekWeatherView(context);
        linearLayout.addView(drawWeekWeatherView);
    }
    public void setDatas(List<WeekWeatherModel> weekWeatherModels ){

            this.drawWeekWeatherView.setDatas(weekWeatherModels);
    }
}