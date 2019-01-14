package com.menksoft.android.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Layout.Alignment;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.menksoft.android.text.MongolianStaticLayout;

import java.net.URL;

public class StaticLayoutTestView extends View {

	private StaticLayout mStaticLayout1;//,mStaticLayout2,mStaticLayout3;
	private int oX;
	private int oY;

	public StaticLayoutTestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Init();
	}

	public StaticLayoutTestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public StaticLayoutTestView(Context context) {
		super(context);
		Init();
	}
	ImageGetter imgGetter = new ImageGetter() {
		 Drawable drawable = null;  
        public Drawable getDrawable(String source) {  
            Log.d("Image Path", source);  
            URL url;  
            try {  
                url = new URL(source);  
                //drawable = getContext().getResources().getDrawable(R.drawable.h);
            } catch (Exception e) {  
                return null;  
            }
			if(drawable!=null)
            drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable  
                    .getIntrinsicWidth());  
            return drawable;  
        }  
    };  
	private void Init() {
		TextPaint paint = new TextPaint();
		paint.setColor(Color.rgb(37,41,53));
		paint.setAntiAlias(true);
		paint.setTextSize(32.0f);
		
		paint.linkColor = Color.BLUE;
		

		if(isInEditMode())
		{
			mStaticLayout1 = new MongolianStaticLayout(
					//mStaticLayout1 = new StaticLayout(
						"1234\n\t56fd gfd gldf gkfd;sl gsdf gdfg f \nf sgfds gdsfgsgfd gdfg df\n",
						//0,30,
						paint, 
						300, 
						Alignment.ALIGN_NORMAL, 
						1.0f, 0.0f, false
						//,ellipsize,200
						);
			return;
		}
		
		TruncateAt ellipsize = TruncateAt.END;
		
		Spanned spanned1= Html.fromHtml(
                "<b >text3:的发 </b>  " +
                "<font color=\"red\">RED Text</font>"+
                "<a href=\"http://cdn.utest.qq.com/main/static/image/index/icon-logo-new.png\">link</a> <br>" +
                "<img src='http://cdn.utest.qq.com/main/static/image/index/icon-logo-new.png'/>"+
                "created in the<br/> Java<br/> source code using HTML dsa ds dsa dsa dsa.",imgGetter,null);
		
		
		
		mStaticLayout1 = new MongolianStaticLayout(
		//mStaticLayout1 = new StaticLayout(
			"1234fhdus 发多少啊发生  范德萨  fjkd kgsd gf gfds",
			//0,30,
			//spanned1,
			paint, 
			300, 
			Alignment.ALIGN_NORMAL, 
			1.0f, 0.0f, false
			//,ellipsize,200
			);
		
//		mStaticLayout2 = new MongolianStaticLayout(
//				//"1234\n5678\nabcdfds dsfsd fds fds hjfdsh fds\nefgh",
//				spanned,
//				paint, 
//				300, 
//				Alignment.ALIGN_CENTER, 
//				1.0f, 0.0f, false
//				);
//		
//		mStaticLayout3 = new MongolianStaticLayout(
//				//"1234\n5678\nabcdfds dsfsd fds fds hjfdsh fds\nefgh",
//				spanned,
//				paint, 
//				300, 
//				Alignment.ALIGN_OPPOSITE, 
//				1.0f, 0.0f, false
//				);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		DrawBaseLine(canvas);

		canvas.save();

		canvas.translate(oX, oY);

		mStaticLayout1.draw(canvas);
		canvas.restore();
		canvas.save();


//		canvas.translate(oX ,oY + 300);
//		mStaticLayout2.draw(canvas);
//		canvas.restore();
//		canvas.save();
//
//		canvas.translate(oX,oY+  600);
//		mStaticLayout3.draw(canvas);
//		canvas.restore();
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		oX = getPaddingLeft()+40;
		oY = getPaddingTop()+40;
	}

	protected void DrawBaseLine(Canvas canvass) {
		
		int perLen = 10;
		Paint paint = new Paint();
		paint.setColor(Color.DKGRAY);//"252935"
		paint.setAntiAlias(false);
		
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		
		canvass.drawLine(oX , oY , w , oY, paint);
		canvass.drawLine(oX , oY , oX , h, paint);
		
		
		for(int i=perLen;i<w-perLen;i+=perLen)
		{
			if(i%100 ==0)
			{
				canvass.drawText(i+"",oX +i - 10,oY- 20, paint);
				canvass.drawLine(oX+i, oY, oX+i, oY-7, paint);
				
			}
			else {
				canvass.drawLine(oX+i, oY, oX+i, oY-3, paint);
			}
		}
		for(int i=perLen;i<h-perLen;i+=perLen)
		{
			if(i%100 ==0)
			{
				canvass.drawText(i+"",oX-30,oY+i+5, paint);
				canvass.drawLine(oX, oY+i, oX+7, oY+i, paint);
				
			}
			else {
				canvass.drawLine(oX, oY+i, oX+3, oY+i, paint);
			}
		}
	}


}
