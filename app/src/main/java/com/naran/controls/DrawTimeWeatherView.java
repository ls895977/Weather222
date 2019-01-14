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

import com.naran.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MENK021 on 2017/1/2.
 */

public class DrawTimeWeatherView extends View {
    private Context context;
    private Paint paint;
    private Paint weatherPaint;
    private Paint redPaint;
    private Typeface typeface;
    private int baseDistance = Dp2Px(56);
    private int baseHeight = Dp2Px(432);
    private List<String> winds = new ArrayList<>();
    private List<String> times = new ArrayList<String>();
    private List<Integer> weathers = new ArrayList<>();
    private List<String> tenger = new ArrayList<>();
    private float scale;
    private Paint bitMapPaint;

    public DrawTimeWeatherView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DrawTimeWeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DrawTimeWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        scale = getResources().getDisplayMetrics().density;
        paint.setTextSize(18.0f*scale);

        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
//        redPaint.setTypeface(typeface);
        redPaint.setTextSize(18.0f*scale);
        redPaint.setColor(Color.WHITE);
        redPaint.setAntiAlias(true);

        weatherPaint = new Paint();
        weatherPaint.setTextSize(18.0f*scale);
        weatherPaint.setColor(Color.GRAY);
        weatherPaint.setAntiAlias(true);

        bitMapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitMapPaint.setFilterBitmap(true);
        bitMapPaint.setDither(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int max = -1000;
        int min = 1000;
        for(int i=0;i<24;i++){
            if(max<weathers.get(i)){
                max = weathers.get(i);
            }
            if(min>weathers.get(i)){
                min = weathers.get(i);
            }
        }
        // 120 dp 差距
        int juree = Math.abs(max - min)+1;
        int hemjee = (int)((Dp2Px(120))/juree);

        for(int i = 0;i<juree;i++){
            canvas.drawText(min+i+"",Dp2Px(20),baseHeight-Dp2Px(80) - hemjee*i,redPaint);
            canvas.drawLine(Dp2Px(8),baseHeight-Dp2Px(80)-hemjee*i,baseDistance*25+Px2Dp(8),baseHeight-Dp2Px(80)-hemjee*i,weatherPaint);
        }
        int xogomMarginLeft = Dp2Px(8);
        for(int i=0;i<26;i++){
            // 竖线
            canvas.drawLine(xogomMarginLeft,Dp2Px(8),xogomMarginLeft,baseHeight-Dp2Px(1),paint);
            canvas.drawText(times.get(i),xogomMarginLeft+Dp2Px(8),baseHeight-Dp2Px(16),redPaint);
            if(i>0&& i<25){
                int sub = baseDistance/2-Dp2Px(4);
//                if(i>9){
//                    sub = Dp2Px(40);
//                }
                canvas.drawText(winds.get(i-1),xogomMarginLeft+sub,baseHeight-Dp2Px(56),redPaint);
            }
            if(i>0 && i<25) {
                canvas.drawText(weathers.get(i - 1) + "", baseDistance * i + baseDistance / 2 , Dp2Px(baseHeight - 80 + ((max - weathers.get(i - 1)) * hemjee) - 120 + hemjee / 2), paint);
                canvas.drawCircle(baseDistance * i + baseDistance / 2 + Dp2Px(8), baseHeight - Dp2Px(2) - Dp2Px(180) + ((max - weathers.get(i - 1)) * hemjee ), Dp2Px(4), paint);
                if (i < 24) {
                    canvas.drawLine(baseDistance * i + baseDistance / 2 + Dp2Px(8), baseHeight - Dp2Px(2) - Dp2Px(180) + ((max - weathers.get(i - 1)) * hemjee ),baseDistance * i+baseDistance + baseDistance / 2 + Dp2Px(8), baseHeight - Dp2Px(2) - Dp2Px(180) + ((max - weathers.get(i)) * hemjee ), paint);
//                    canvas.drawLine(baseDistance * i + baseDistance / 2 + Dp2Px(8), baseHeight -Dp2Px( 80 + ((max - weathers.get(i - 1)) * hemjee)) - Dp2Px(120) + hemjee), baseDistance * (i + 1) + baseDistance / 2 + 8, Dp2Px(baseHeight - 80 + ((max - weathers.get(i)) * hemjee) - 120 + hemjee), paint);
                }
                Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.weather);
                Rect src = new Rect();

                Rect mDestRect = new Rect(baseDistance* i+Dp2Px(10), Dp2Px(2),baseDistance* i+Dp2Px(60), Dp2Px(50));
                canvas.drawBitmap(bitmap,null,mDestRect,bitMapPaint);
                canvas.save();
                canvas.rotate(90.0f);
                canvas.drawText(tenger.get(i-1),Dp2Px(56),(baseDistance * i + baseDistance / 2 )*-1,paint);
                canvas.restore();
            }
            xogomMarginLeft += baseDistance;
        }

        canvas.drawLine(Dp2Px(8),baseHeight-Dp2Px(200),baseDistance*25+Dp2Px(8),baseHeight-Dp2Px(200),paint);//温度上面的
        canvas.drawLine(Dp2Px(8),baseHeight-Dp2Px(80),baseDistance*25+Dp2Px(8),baseHeight-Dp2Px(80),paint); // 风上面的
        canvas.drawLine(Dp2Px(8),baseHeight-Dp2Px(40),baseDistance*25+Dp2Px(8),baseHeight-Dp2Px(40),paint); // 时间上面的
        canvas.drawLine(Dp2Px(8),baseHeight-Dp2Px(1),baseDistance*25+Dp2Px(8),baseHeight-Dp2Px(1),paint); // xia
        canvas.drawLine(Dp2Px(8),Dp2Px(8),baseDistance*25+Dp2Px(8),Dp2Px(8),paint); // shang
        canvas.save();
        canvas.rotate(90.0f);
        canvas.drawText("",Dp2Px(356),Dp2Px(-30),paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = View.MeasureSpec.getSize(Dp2Px(baseHeight));
        int width = View.MeasureSpec.getSize(baseDistance*25+Dp2Px(16));
        setMeasuredDimension(width,height);
    }
    public int Dp2Px( float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(float px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
    private void initTime(){
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
    public void setWinds(){

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
        weathers.add(-18);
        weathers.add(-19);
        weathers.add(-20);
        weathers.add(-21);
        weathers.add(-22);
        weathers.add(-23);
        weathers.add(-24);
        weathers.add(-23);
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

        tenger.add(" ");
        tenger.add(" ");
        tenger.add("  ");
        tenger.add("");
        tenger.add("");
        tenger.add("");
        tenger.add("");
        tenger.add(" ");
        tenger.add(" ");
        tenger.add("  ");
        tenger.add("");
        tenger.add("");
        tenger.add("");
        tenger.add("");
        tenger.add(" ");
        tenger.add(" ");
        tenger.add("  ");
        tenger.add("");
        tenger.add("");
        tenger.add(" ");
        tenger.add(" ");
        tenger.add("  ");
        tenger.add("");
        tenger.add("");
    }
}