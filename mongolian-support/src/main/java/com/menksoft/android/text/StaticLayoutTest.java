package com.menksoft.android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticLayoutTest extends StaticLayout{

	private static String logString;

	public StaticLayoutTest(CharSequence source, TextPaint paint, int width,
			Alignment align, float spacingmult, float spacingadd,
			boolean includepad) {
		super(source, paint, width, align, spacingmult, spacingadd, includepad);
		// TODO Auto-generated constructor stub
	}

	public StaticLayoutTest(CharSequence source, int bufstart, int bufend,
			TextPaint paint, int outerwidth, Alignment align,
			float spacingmult, float spacingadd, boolean includepad,
			TruncateAt ellipsize, int ellipsizedWidth) {
		super(source, bufstart, bufend, paint, outerwidth, align, spacingmult,
				spacingadd, includepad, ellipsize, ellipsizedWidth);
		// TODO Auto-generated constructor stub
	}

	public StaticLayoutTest(CharSequence source, int bufstart, int bufend,
			TextPaint paint, int outerwidth, Alignment align,
			float spacingmult, float spacingadd, boolean includepad) {
		super(source, bufstart, bufend, paint, outerwidth, align, spacingmult,
				spacingadd, includepad);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public void draw(Canvas c, Path highlight, Paint highlightPaint,
			int cursorOffsetVertical) {
		// TODO Auto-generated method stub
		//super.draw(c, highlight, highlightPaint, cursorOffsetVertical);
		
		
		/*---------------mWorkingPaint----------------*/
		TextPaint mWorkingPaint = getWorkingPaint();
		if(mWorkingPaint!=null)
		{
			c.drawText("mWorkingPaint is working fine!", 40, 20, getPaint());
		}
		else{
			c.drawText(logString, 0, 0, getPaint());
			logString="";
		}
		
		/*-----------GetDirections_mDirections --------------------------*/
		try {
			int [] dirs = GetDirections_mDirections(getLineDirections(0));
			if(dirs!=null)
			{
				c.drawText("GetDirections_mDirections is working fine!", 40, 20, getPaint());
			}
			else{
				c.drawText(logString, 0, 0, getPaint());
				logString="";
			}
		} catch (Exception e) {
			c.drawText(e.getMessage(), 0, 0, getPaint());
		}
		
		
		
		//c.drawText(text, x, y, paint)
	}

	private TextPaint getWorkingPaint() {
		// TODO Auto-generated method stub
		Field field;
		try {
			field= MongolianStaticLayout.class
					.getSuperclass()
					.getSuperclass().getDeclaredField("mWorkPaint");
			field.setAccessible(true);
			return (TextPaint)field.get(this);
		} catch (SecurityException e) {
			logString = e.getMessage();
		} catch (NoSuchFieldException e) {
			
			logString = e.getMessage();
		} catch (IllegalArgumentException e) {
			
			logString = e.getMessage();
		} catch (IllegalAccessException e) {
			
			logString = e.getMessage();
		}
		return null;
	}

	
	public int[] GetDirections_mDirections(Directions directions)
	{
		Field field;
		try {
			field= Directions.class.getDeclaredField("mDirections");
			field.setAccessible(true);
			return (int[])field.get(directions);
		} catch (SecurityException e) {
			
			logString = e.getMessage();
		} catch (NoSuchFieldException e) {
			
			logString = e.getMessage();
		} catch (IllegalArgumentException e) {
			
			logString = e.getMessage();
		} catch (IllegalAccessException e) {
			
			logString = e.getMessage();
		}
		return null;
	}
	
	public static float Styled_drawText(
			Canvas canvas,
            CharSequence text, int start, int end,
            int dir, boolean runIsRtl,
            float x, int top, int y, int bottom,
            TextPaint paint,
            TextPaint workPaint,
            boolean needWidth){
		
		
		Class [] parameterTypes=new Class[]{
			Canvas.class,
            CharSequence.class, Integer.class,Integer.class,
            Integer.class, Boolean.class,
            Float.class, Integer.class, Integer.class, Integer.class,
            TextPaint.class,
            TextPaint.class,
            Boolean.class
				};
		try {
			Method method = Styled.class.getMethod("drawText",parameterTypes);
			method.setAccessible(true);
			return (Float) method.invoke(null, 
					canvas,
		            text, start, end,
		            dir, runIsRtl,
		            x, top, y, bottom,
		            paint,
		            workPaint,
		            needWidth);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			logString = e.getMessage();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logString = e.getMessage();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logString = e.getMessage();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			logString = e.getMessage();
		}
		return 0;
	}
	
	
	private float Layout_measureText(TextPaint mPaint, TextPaint mWorkPaint,
			CharSequence mText, int start, int end, Object object, boolean tab,
			Object[] tabs) {
		Class [] parameterTypes=new Class[]{
				TextPaint.class,
                TextPaint.class,
                CharSequence.class,
                int.class, int.class,
                Paint.FontMetricsInt.class,
                boolean.class, Object[].class
					};
			try {
				Method method = Layout.class.getMethod("measureText",parameterTypes);
				method.setAccessible(true);
				return (Float) method.invoke(null, 
						mPaint, mWorkPaint,
						mText, start, end, object, tab,
						tabs);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			}
		return 0;
	}
	
	
	private float Layout_nextTab(CharSequence text, int start, int end, float f,
			Object[] parspans) {
		// TODO Auto-generated method stub
		Class [] parameterTypes=new Class[]{
				CharSequence.class, int.class, int.class,
                float.class, Object[].class
					};
			try {
				Method method = Layout.class.getMethod("nextTab",parameterTypes);
				method.setAccessible(true);
				return (Float) method.invoke(null, 
						text, start, end, f,
						parspans);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logString = e.getMessage();
			}
		return 0;
	}
	
	private Directions getDIRS_ALL_LEFT_TO_RIGHT() {
		// TODO Auto-generated method stub
		
		Field field;
		try {
			field= Layout.class.getDeclaredField("DIRS_ALL_LEFT_TO_RIGHT");
			field.setAccessible(true);
			return (Directions) field.get(null);
		} catch (SecurityException e) {
			
			logString = e.getMessage();
		} catch (NoSuchFieldException e) {
			
			logString = e.getMessage();
		} catch (IllegalArgumentException e) {
			
			logString = e.getMessage();
		} catch (IllegalAccessException e) {
			
			logString = e.getMessage();
		}
		return null;
	}
}
