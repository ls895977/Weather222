package com.menksoft.android.widget.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

public class PointCursorView extends View {

	public TextView widget;
	public int zoomX, zoomY;
	float lineWidth;
	private Paint paint;

	public PointCursorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setFilterBitmap(true);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int w = PointCursorController.ZoomerViewWidth;
		float radios = w /2.0f ;
		int clipX = (int) (zoomX - (lineWidth + w) / 2);
		int clipY = (int) (zoomY - radios);
		if(clipY<0)
			clipY = 0;
		if(clipX<0)
			clipX = 0;
		widget.destroyDrawingCache();
		Bitmap cacheBitmap = widget.getDrawingCache();

		if (cacheBitmap != null) {
			int[] pixels = new int[w * w];
			cacheBitmap.getPixels(pixels, 0, w, clipX, clipY, w, w);

			canvas.drawCircle(radios, radios, radios, paint);

			Bitmap newBitmap = Bitmap.createBitmap(pixels, 0, w, w, w,
					Config.ARGB_8888);


			canvas.drawBitmap(getclip(newBitmap), 0, 0, paint);

			paint.setStyle(Style.STROKE);
			 canvas.drawCircle(radios, radios, radios, paint);
		}
	}

	public Bitmap getclip(Bitmap bitmap) {
		int w = PointCursorController.ZoomerViewWidth;
		float radios = w/2.0f;
		Bitmap output = Bitmap.createBitmap(w, w, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, w);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		canvas.drawCircle(radios,radios, radios, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}
