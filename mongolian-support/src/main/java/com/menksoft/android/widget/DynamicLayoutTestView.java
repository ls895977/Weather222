package com.menksoft.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.menksoft.android.text.MongolianDynamicLayout;

public class DynamicLayoutTestView extends View{
	
	DynamicLayout mDynamicLayout;

	public DynamicLayoutTestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Init();
	}

	public DynamicLayoutTestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Init();
	}

	public DynamicLayoutTestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		Init();
	}
	
	private void Init() {
		TextPaint paint = new TextPaint();
		paint.setColor(Color.rgb(37,41,53));
		paint.setAntiAlias(true);
		paint.setTextSize(32.0f);
		
		paint.linkColor = Color.BLUE;
		
		editable = SpannableStringBuilder.valueOf("Yes! Yes! dasdas dasd asdas das dsa");
		
		mDynamicLayout = new MongolianDynamicLayout(
		//mStaticLayout1 = new StaticLayout(
				editable,
				//"1234\n\t56��������78\n����abcdfds dsfsd fds fds hjfdsh fds\nefgh",
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


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		DrawBaseLine(canvas);

		canvas.save();

		
		canvas.translate(oX, oY);
		

		mDynamicLayout.draw(canvas,null,null,0);
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

	private int oX;
	private int oY;
	private Editable editable;

	public void setText(CharSequence s) {
		// TODO Auto-generated method stub
		if(s.toString().startsWith("e"))
			editable.append(s.toString());
		else {
			editable.insert(0, s.toString());
		}
		this.invalidate();
	}


}
