package com.menksoft.android.widget.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.menksoft.android.widget.MongolianTextView;


public class PointCursorController extends PopupWindow {
	static MongolianTextView mTextView;
	static PointCursorController pointCursorController;
	static PointCursorView controlView;
	private static int mPositionY;
	private static int mPositionX;

	public static int ZoomerViewWidth;

	public PointCursorController(View view, int x, int y) {
		// TODO Auto-generated constructor stub
		super(view, x, y);
	}

	@SuppressLint("NewApi")
	public static PointCursorController getInstance(MongolianTextView tv) {
		mTextView = tv;
		Context context = tv.getContext();

		ZoomerViewWidth = context.getResources().getDimensionPixelSize(
				0);

		controlView = new PointCursorView(context);
		controlView.setLayoutParams(new LayoutParams(ZoomerViewWidth,
				ZoomerViewWidth));
		pointCursorController = new PointCursorController(controlView,
				ZoomerViewWidth, ZoomerViewWidth);
		pointCursorController.setSplitTouchEnabled(true);
		pointCursorController.setClippingEnabled(false);
		pointCursorController
				.setWindowLayoutType(WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);

		return pointCursorController;
	}

	static void showAtPosition(MongolianTextView tv, int x, int y,
			float lineWidth) {
		if (pointCursorController == null)
			getInstance(tv);
		controlView.widget = tv;
		controlView.zoomX = mPositionX;
		controlView.zoomY = mPositionY;
		controlView.lineWidth = lineWidth;

		int dispX = x - ZoomerViewWidth;
		int dispY = y - ZoomerViewWidth;
		controlView.invalidate();
		if (pointCursorController.isShowing()) {
			int[] location = new int[2];
			tv.getLocationInWindow(location);
			pointCursorController.update(dispX + location[0], dispY
					+ location[1], -1, -1);
		} else {
			pointCursorController.showAsDropDown(tv, dispX,
					dispY - tv.getMeasuredHeight());
		}
	}

	static public void hide() {
		if (pointCursorController == null)
			return;
		else if (pointCursorController.isShowing()) {
			pointCursorController.dismiss();
		}
	}

	public static void show(MongolianTextView widget, int x, int y,
			float lineWidth) {
		// TODO Auto-generated method stub
		int offset = widget.getSelectionEnd();
		if (offset != widget.getSelectionStart()) {
			hide();
			return;
		}
		positionAtCursorOffset(widget, offset,
				widget.getScrollX() * widget.getScrollY() != 0);
		Log.e("#CC", offset + "-> showing at position " + mPositionX + ","
				+ mPositionY);
		showAtPosition(widget, x, y, lineWidth);
	}

	protected static void positionAtCursorOffset(MongolianTextView widget,
			int offset, boolean parentScrolled) {
		// A HandleView relies on the layout, which may be nulled by external
		// methods
		Layout layout = widget.getLayout();
		if (layout == null) {
			return;
		}

		final int line = layout.getLineForOffset(offset);

		mPositionY = (int) (layout.getPrimaryHorizontal(offset) - 0.5f);
		mPositionX = layout.getLineBottom(line);
		// Take TextView's padding and scroll into account.
		mPositionX += widget.getCompoundPaddingLeft() - widget.getScrollX();
		mPositionY += widget.getCompoundPaddingTop() - widget.getScrollY();

	}

}
