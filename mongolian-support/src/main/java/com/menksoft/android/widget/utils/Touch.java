package com.menksoft.android.widget.utils;

import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class Touch {
	private static int META_SELECTING = 0x800;

	private static class DragState implements NoCopySpan {
		public float mX;
		public float mY;
		public int mScrollX;
		public int mScrollY;
		public boolean mFarEnough;
		public boolean mUsed;

		public DragState(float x, float y, int scrollX, int scrollY) {
			mX = x;
			mY = y;
			mScrollX = scrollX;
			mScrollY = scrollY;
		}
	}

	public static boolean onTouchEvent(TextView widget, Spannable buffer,
			MotionEvent event) {
		DragState[] ds;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			ds = buffer.getSpans(0, buffer.length(), DragState.class);

			for (int i = 0; i < ds.length; i++) {
				buffer.removeSpan(ds[i]);
			}

			buffer.setSpan(
					new DragState(event.getX(), event.getY(), widget
							.getScrollX(), widget.getScrollY()), 0, 0,
					Spannable.SPAN_MARK_MARK);
			return true;

		case MotionEvent.ACTION_UP:
			ds = buffer.getSpans(0, buffer.length(), DragState.class);

			for (int i = 0; i < ds.length; i++) {
				buffer.removeSpan(ds[i]);
			}

			if (ds.length > 0 && ds[0].mUsed) {
				return true;
			} else {
				return false;
			}

		case MotionEvent.ACTION_MOVE:
			ds = buffer.getSpans(0, buffer.length(), DragState.class);

			if (ds.length > 0) {
				if (ds[0].mFarEnough == false) {
					int slop = ViewConfiguration.get(widget.getContext())
							.getScaledTouchSlop();

					if (Math.abs(event.getX() - ds[0].mX) >= slop
							|| Math.abs(event.getY() - ds[0].mY) >= slop) {
						ds[0].mFarEnough = true;
					}
				}

				if (ds[0].mFarEnough) {
					ds[0].mUsed = true;
					boolean cap = (MetaKeyKeyListener.getMetaState(buffer,
							KeyEvent.META_SHIFT_ON) == 1)
							|| (MetaKeyKeyListener.getMetaState(buffer,
									META_SELECTING) != 0);
					float dx;
					float dy;
					if (cap) {
						// if we're selecting, we want the scroll to go in
						// the direction of the drag
						dx = event.getX() - ds[0].mX;
						dy = event.getY() - ds[0].mY;
					} else {
						dx = ds[0].mX - event.getX();
						dy = ds[0].mY - event.getY();
					}
					ds[0].mX = event.getX();
					ds[0].mY = event.getY();

					int nx = widget.getScrollX() + (int) dx;
					int ny = widget.getScrollY() + (int) dy;

					int padding = widget.getTotalPaddingLeft()
							+ widget.getTotalPaddingRight();
					Layout layout = widget.getLayout();
					
					nx = Math.min(nx, layout.getHeight()
							- (widget.getWidth() - padding));
					nx = Math.max(nx, 0);

					int oldX = widget.getScrollX();
					int oldY = widget.getScrollY();

					scrollTo(widget, layout, nx, ny);

					// If we actually scrolled, then cancel the up action.
					if (oldX != widget.getScrollX()
							|| oldY != widget.getScrollY()) {
						widget.cancelLongPress();
					}

					return true;
				}
			}
		}

		return false;
	}

	public static void scrollTo(TextView widget, Layout layout, int x, int y) {
		int padding = widget.getTotalPaddingLeft()
				+ widget.getTotalPaddingRight();
		int left = layout.getLineForVertical(x);
		int right = layout.getLineForVertical(x + widget.getWidth() - padding);

		int top = Integer.MAX_VALUE;
		int bottom = 0;
		Alignment a = null;

		for (int i = left; i <= right; i++) {
			top = (int) Math.min(top, layout.getLineLeft(i));
			bottom = (int) Math.max(bottom, layout.getLineRight(i));

			if (a == null) {
				a = layout.getParagraphAlignment(i);
			}
		}
		padding = widget.getCompoundPaddingTop() + widget.getCompoundPaddingTop();
		int height = widget.getHeight();
		int diff = 0;
		if (bottom - top < height - padding) {
			if (a == Alignment.ALIGN_CENTER) {
				diff = (height - padding - (bottom - top)) / 2;
			} else if (a == Alignment.ALIGN_OPPOSITE) {
				diff = height - padding - (bottom - top);
			}
		}

		y = Math.min(y, bottom - (height - padding) - diff);
		y = Math.max(y, top - diff);

		widget.scrollTo(x, y);
	}
	public static int getInitialScrollX(TextView widget, Spannable buffer) {
        DragState[] ds = buffer.getSpans(0, buffer.length(), DragState.class);
        return ds.length > 0 ? ds[0].mScrollX : -1;
    }
    
    public static int getInitialScrollY(TextView widget, Spannable buffer) {
        DragState[] ds = buffer.getSpans(0, buffer.length(), DragState.class);
        return ds.length > 0 ? ds[0].mScrollY : -1;
    }
}
