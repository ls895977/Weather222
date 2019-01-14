package com.menksoft.android.text;

import android.graphics.Canvas;
import android.text.Layout.Directions;
import android.text.TextPaint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TextLineEmulator {
	
	public static Class TextLineClass;
	
	
	static{
		if(TextLineClass==null){
			try {
				TextLineClass =  Class.forName("android.text.TextLine");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//TextLine.obtain();
	public static Object obtain(){
		
		try {
			Method obtainMethod= TextLineClass.getDeclaredMethod("obtain", new Class[]{});
			obtainMethod.setAccessible(true);
			return obtainMethod.invoke(null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void set(Object tl,TextPaint paint, CharSequence text, int start, int limit, int dir,
            Directions directions, boolean hasTabs, Object tabStops)
	{
		try {
			Class TabStopsClass = Class.forName("android.text.Layout$TabStops");
			Method setMethod= 
					TextLineClass.getDeclaredMethod("set", 
							new Class[]{
							TextPaint.class, CharSequence.class, int.class, int.class, int.class,
				            Directions.class, boolean.class, TabStopsClass
					});
			setMethod.setAccessible(true);
			setMethod.invoke(tl,paint, text, start, limit, dir,
		            directions, hasTabs, tabStops);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void draw(Object tl,Canvas c, float x, int top, int y, int bottom){
		try {
			Method drawMethod= 
					TextLineClass.getDeclaredMethod("draw", 
							new Class[]{
							Canvas.class, float.class, int .class, int.class, int.class
					});
			drawMethod.setAccessible(true);
			drawMethod.invoke(tl,c, x, top, y, bottom);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
