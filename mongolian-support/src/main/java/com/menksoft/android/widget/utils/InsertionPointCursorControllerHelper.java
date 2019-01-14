package com.menksoft.android.widget.utils;

import android.os.Build.VERSION;
import android.widget.TextView;

import java.lang.reflect.Field;

public class InsertionPointCursorControllerHelper {

	
	public static boolean SetControllerPosition(TextView widget,float x,float y){
		
		Object mInsertionPointCursorController = get_mInsertionPointCursorController(widget);
		
		if(mInsertionPointCursorController==null)
			return false;
		
		Object mHandle = get_mHandle(mInsertionPointCursorController);
		
		if(mHandle==null)
			return false;
		
		return setHandleViewPosition(mHandle,x,y);

	}
	
	static Object get_mInsertionPointCursorController(TextView widget){
		if (VERSION.SDK_INT < 17) {
			try {
				Field field = TextView.class
						.getDeclaredField("mInsertionPointCursorController");
				field.setAccessible(true);
				return field.get(widget);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			Field field_mShowCursor;
			Class class_Editor;
			try {
				class_Editor = Class.forName("android.widget.Editor");
				Field field_mEditor;
				field_mShowCursor = class_Editor.getDeclaredField(
						"mInsertionPointCursorController");
				field_mShowCursor.setAccessible(true);
				
				field_mEditor =TextView.class.getDeclaredField("mEditor");
				field_mEditor.setAccessible(true);
				Object editor = field_mEditor.get(widget);
				if(editor==null)
					return 0;
				return field_mShowCursor.get(editor);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	static Class InsertionPointCursorControllerClass ;
	static Object get_mHandle(Object controllerObject){
		
		if(InsertionPointCursorControllerClass==null)
		{
			try {
				String classSource = "";
				if(VERSION.SDK_INT<17)
					classSource = "android.widget.TextView$InsertionPointCursorController";
				else
					classSource = "android.widget.Editor$InsertionPointCursorController";
				InsertionPointCursorControllerClass = Class.forName(classSource);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		try {
			Field field = InsertionPointCursorControllerClass.getDeclaredField("mHandle");
			field.setAccessible(true);
			return field.get(controllerObject);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	static Class InsertionHandleViewClass ;
	public static boolean setHandleViewPosition(Object handle,float x,float y)
	{
		if(InsertionHandleViewClass==null){
			try {
				String classPath = "android.widget.Editor$InsertionHandleView";
				if(VERSION.SDK_INT<17)
					classPath = "android.widget.TextView$InsertionHandleView";
				InsertionHandleViewClass = Class.forName(classPath);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			Field xField = InsertionHandleViewClass.getDeclaredField("mDownPositionX");
			xField.setAccessible(true);
			xField.set(handle, x);
			
			Field yField = InsertionHandleViewClass.getDeclaredField("mDownPositionY");
			yField.setAccessible(true);
			yField.set(handle, y);
			
			return true;
			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
