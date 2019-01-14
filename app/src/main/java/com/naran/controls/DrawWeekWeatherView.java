package com.naran.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.naran.model.WeekWeatherModel;
import com.naran.weather.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MENK021 on 2017/1/3.
 */

public class DrawWeekWeatherView extends View {
    private Context context;
    private Paint paint;
    private Paint weatherPaint;
    private Paint redPaint;
    private Typeface typeface;
    private int baseDistance = Dp2Px(80);
    private int baseHeight = Dp2Px(440);

    List<String> drawableID; // 天气图片
    List<String> dolaach;// 温度
    List<String> jug; // 风向
    List<String> salhinDes; // 风力

    private List<String> winds = new ArrayList<>();
    private List<String> times = new ArrayList<String>();
    private List<Integer> weathers = new ArrayList<>();
    private List<String> tenger = new ArrayList<>();
    private List<String> garag = new ArrayList<>();
    private int textStartPoint = Dp2Px(64);// 上午下午天气情况的起点位置
    private List<WeekWeatherModel> weekWeatherModels;

    public DrawWeekWeatherView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DrawWeekWeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DrawWeekWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        setWinds();
        initTime();
        Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/mkhw.ttf");
        paint = new Paint();
        redPaint = new Paint();
        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        paint.setTextSize(Dp2Px(15));
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        redPaint.setTypeface(typeface);
        redPaint.setTextSize(Dp2Px(15));
        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(true);

        weatherPaint = new Paint();
        weatherPaint.setTextSize(Dp2Px(15));
        weatherPaint.setColor(Color.GRAY);
        weatherPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(weekWeatherModels == null){
            return;
        }
        int xogomMarginLeft = Dp2Px(8);
        int jump = 0;

        for (int i = 0; i < 6; i++) {

            canvas.drawLine(xogomMarginLeft, Dp2Px(8), xogomMarginLeft, baseHeight - Dp2Px(3), paint);// 大分割竖线
            // 天气分割线 小短线
//            canvas.drawLine(xogomMarginLeft + baseDistance / 2, baseHeight - Dp2Px(355), xogomMarginLeft + baseDistance / 2, baseHeight - Dp2Px(160), paint);//
            if (i > 0) {
                // 日期
                canvas.drawText(this.weekWeatherModels.get(i - 1).getForecastDate(), baseDistance * (i - 1) + baseDistance / 2 - Dp2Px(30), Dp2Px(80), paint);// 日期
                //温度
                canvas.drawText(this.weekWeatherModels.get(i - 1).getLowTemperature() + "~" + this.weekWeatherModels.get(i - 1).getHighTemperature() + "℃", baseDistance * (i - 1) + baseDistance / 2 - Dp2Px(13), Dp2Px(295), paint);
                //  图标
                // 图标
                String bitmapNameEdur = "w" + weekWeatherModels.get(i - 1).getWeatherPhenomenonID() + "";
                Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), getImageResourceId(bitmapNameEdur));
                Rect mDestRect = new Rect(baseDistance * (i - 1) + Dp2Px(28), Dp2Px(85), baseDistance * (i - 1) + Dp2Px(68), Dp2Px(125));
                canvas.drawBitmap(bitmap, null, mDestRect, paint);
                // 第二个图标
//                Bitmap bitmap1 = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.w0);
//                Rect mDestRect1 = new Rect(baseDistance * (i - 1) + Dp2Px(15) + baseDistance / 2, Dp2Px(85), baseDistance * (i - 1) + Dp2Px(55) + baseDistance / 2, Dp2Px(125));
//                canvas.drawBitmap(bitmap1, null, mDestRect1, paint);
            }
            canvas.save();
            canvas.rotate(90.0f);

            if (i > 0) {
                // 星期
                canvas.drawText("", Dp2Px(24), (baseDistance * (i - 1) + baseDistance / 2 - Dp2Px(20)) * -1, paint);
                canvas.drawText("", Dp2Px(24), (baseDistance * (i - 1) + baseDistance / 2) * -1, paint);
                Calendar c = Calendar.getInstance();
                int way = c.get(Calendar.DAY_OF_WEEK) -1;//星期
                canvas.drawText(garag.get(way+i-1), Dp2Px(24), (baseDistance * (i - 1) + baseDistance / 2 + Dp2Px(20)) * -1, paint);

                // 天气 第一个
                int addLeftPoint = Dp2Px(-23);// 如果两行需要换行，剧中

                String[] split1 = tenger.get(i - 1 + jump).split(" ");
                float scale = getResources().getDisplayMetrics().density;
                if (scale < 2.0) {
                    textStartPoint = Dp2Px(90);
                } else if (scale == 2.0) {
                    textStartPoint = Dp2Px(65);
                } else {
                    textStartPoint = Dp2Px(45);
                }
                if (split1.length > 3) {
                    addLeftPoint = Dp2Px(10);
                    String str1 = split1[0] + " " + split1[1];
                    String str2 = "";
                    for (int j = 2; j < split1.length; j++) {
                        str2 += split1[j] + " ";
                    }
                    canvas.drawText(str1, Dp2Px(textStartPoint), addLeftPoint + (baseDistance / 4) * -1 - (i - 1) * baseDistance, paint);
                    canvas.drawText(str2, Dp2Px(textStartPoint), -addLeftPoint + (baseDistance / 4) * -1 - (i - 1) * baseDistance, paint);
                } else {
                    canvas.drawText(tenger.get(i - 1 + jump), Dp2Px(textStartPoint), addLeftPoint + (baseDistance / 4) * -1 - (i - 1) * baseDistance, paint);
                }
                // 天气 第2个

//                String[] split2 = tenger.get(i + jump).split(" ");
//
//                if (split2.length > 3) {
//                    addLeftPoint = Dp2Px(10);
//                    String str1 = split2[0] + " " + split2[1];
//                    String str2 = "";
//                    for (int j = 2; j < split2.length; j++) {
//                        str2 += split2[j] + " ";
//                    }
//                    canvas.drawText(str1, Dp2Px(textStartPoint), addLeftPoint + (baseDistance / 4) * -3 - (i - 1) * baseDistance, paint);
//                    canvas.drawText(str2, Dp2Px(textStartPoint), -addLeftPoint + (baseDistance / 4) * -3 - (i - 1) * baseDistance, paint);
//                } else {
//                    canvas.drawText(tenger.get(i + jump), Dp2Px(textStartPoint), (baseDistance / 4) * -3 - (i - 1) * baseDistance, paint);
//                }
                int addStartPoint = 0;
                if (scale < 2.0) {
                    addStartPoint = -Dp2Px(25);
                }
                if (scale > 2.0) {
                    addStartPoint = Dp2Px(25);
                }
                String winSeedMS = weekWeatherModels.get(i).getWindSpeedMS()+"m/s";
                if(weekWeatherModels.get(i).getWindSpeedMS() == 0){
                    winSeedMS = "";
                }
                canvas.drawText(weekWeatherModels.get(i).getWindDirection(), addStartPoint + textStartPoint + Dp2Px(240), (baseDistance / 4) * -1 - (i - 1) * baseDistance-Dp2Px(12), paint);
                canvas.drawText(""+"  "+winSeedMS, addStartPoint + textStartPoint + Dp2Px(240), (baseDistance * (i - 1) + baseDistance / 2) * -1-Dp2Px(18), paint);

                canvas.restore();
                jump++;
            }

            canvas.restore();
            canvas.save();
            xogomMarginLeft += baseDistance;
        }
//        canvas.drawLine(xogomMarginLeft+baseDistance,10,xogomMarginLeft+baseDistance,Dp2Px(baseHeight-1),paint);
        canvas.drawLine(Dp2Px(8), baseHeight - Dp2Px(355), baseDistance * 5 + Dp2Px(8), baseHeight - Dp2Px(355), paint);//天气上面的
        canvas.drawLine(Dp2Px(8), baseHeight - Dp2Px(160), baseDistance * 5 + Dp2Px(8), baseHeight - Dp2Px(160), paint); // 温度上面的
        canvas.drawLine(Dp2Px(8), baseHeight - Dp2Px(140), baseDistance * 5 + Dp2Px(8), baseHeight - Dp2Px(140), paint); // 温度下面的
        canvas.drawLine(Dp2Px(8), Dp2Px(baseHeight - 138), baseDistance * 5 + Dp2Px(8), Dp2Px(baseHeight - 138), paint); // 时间上面的
        canvas.drawLine(Dp2Px(8), baseHeight - Dp2Px(3), baseDistance * 5 + Dp2Px(8), baseHeight - Dp2Px(3), paint); // xia
        canvas.drawLine(Dp2Px(8), Dp2Px(8), baseDistance * 5 + Dp2Px(8), Dp2Px(8), paint);
        canvas.save();
        canvas.rotate(90.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = View.MeasureSpec.getSize(baseHeight);
        int width = View.MeasureSpec.getSize(baseDistance * 5 + Dp2Px(16));
        setMeasuredDimension(width, height);
    }

    public int Dp2Px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(float px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public int px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public int sp2xp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue * fontScale + 0.5f);
    }

    private void initTime() {
        times.add("");
        times.add("00:00");
        times.add("01:00");
        times.add("02:00");
        times.add("03:00");
        times.add("04:00");
        times.add("05:00");
        times.add("06:00");
        times.add("07:00");
        times.add("08:00");
        times.add("09:00");
        times.add("10:00");
        times.add("11:00");
        times.add("12:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");
        times.add("20:00");
        times.add("21:00");
        times.add("22:00");
        times.add("23:00");
        times.add("24:00");
        times.add("");
        times.add("");
    }

    public void setWinds() {

        winds.add("1");
        winds.add("2");
        winds.add("3");
        winds.add("4");
        winds.add("5");
        winds.add("6");
        winds.add("7");
        winds.add("8");
        winds.add("9");
        winds.add("10");
        winds.add("11");
        winds.add("12");
        winds.add("13");
        winds.add("14");
        winds.add("15");
        winds.add("16");
        winds.add("17");
        winds.add("18");
        winds.add("19");
        winds.add("20");
        winds.add("21");
        winds.add("22");
        winds.add("23");
        winds.add("24");
        //////////////
        weathers.add(-20);
        weathers.add(-19);
        weathers.add(-22);
        weathers.add(-21);
        weathers.add(-20);
        weathers.add(-19);
        weathers.add(-20);
        weathers.add(-22);
        weathers.add(-19);
        weathers.add(-18);
        weathers.add(-20);
        weathers.add(-24);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);
        weathers.add(-20);


        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");
        garag.add("");


        dolaach = new ArrayList<>();
        dolaach.add("5-6℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
        dolaach.add("7-8℃");
    }

    //根据名字获取图片资源
    public int getImageResourceId(String name) {
        R.drawable drawables = new R.drawable();
        //默认的id
        int resId = R.drawable.w0;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field = R.drawable.class.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }

    public void setDatas(List<WeekWeatherModel> weekWeatherModels) {

        this.weekWeatherModels = weekWeatherModels;
        tenger.clear();
        for (int k = 0; k < weekWeatherModels.size(); k++) {
            tenger.add(weekWeatherModels.get(k).getWeatherPhenomenon());
            tenger.add("  "+weekWeatherModels.get(k).getAreaNO());
        }
        invalidate();
    }
}
