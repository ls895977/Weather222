package com.menksoft.android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.BoringLayout;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.menksoft.android.text.MongolianDynamicLayout;
import com.menksoft.android.text.MongolianStaticLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MongolianTextView extends TextView {
	private static final int LINES = 1;
	private int mMaximum = Integer.MAX_VALUE;
	private int mMaxMode = LINES;
	private int mMinimum = 0;
	private int mMinMode = LINES;
	private static final int EMS = LINES;
	private static final int PIXELS = 2;

	private static final int BLINK = 500;

	private static final int PREDRAW_NOT_REGISTERED = 0;
	private static final int PREDRAW_DONE = 2;
	
	private static final int ANIMATED_SCROLL_GAP = 250;

	private int mMaxWidth = Integer.MAX_VALUE;
	private int mMaxWidthMode = PIXELS;
	private int mMinWidth = 0;
	private int mMinWidthMode = PIXELS;
	private boolean mEatTouchRelease;

	private static final BoringLayout.Metrics UNKNOWN_BORING = new BoringLayout.Metrics();

	// XXX should be much larger
	private static final int VERY_WIDE = 16384;
	private static final String TAG = "MongolianTextView";

	public MongolianTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MongolianTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MongolianTextView(Context context) {
		super(context);
	}

	@Override
	protected int computeHorizontalScrollRange() {
		Layout mLayout = getLayout();
		if (mLayout != null)
		{
            return mLayout.getHeight();
		}
		return this.getWidth();
	}

	@Override
	protected int computeVerticalScrollRange() {
		Layout mLayout = getLayout();
		if (mLayout != null)
            return mLayout.getWidth();
		
		return this.getHeight();
	}

    static Field field_mLayout;
    void setLayout(Layout layout){
        if(field_mLayout ==null){
            try {
                field_mLayout = MongolianTextView.class.getSuperclass().getDeclaredField("mLayout");
                field_mLayout.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        try {
            field_mLayout.set(this, layout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	void SetValue(String name, Object value) {

		Field field;
		try {
			field = MongolianTextView.class.getSuperclass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(this, value);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
				e.printStackTrace();
		}
    }

    /**
     * Set the scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     *
     * @param x the x position to scroll to
     * @param y the y position to scroll to
     */
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, 0);
    }

    //閿熸枻鎷峰彇閿熸枻鎷烽敓鏂ゆ嫹鍋忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 Gravity 閿熸枻鎷穕eft-center-right閿熸枻鎷�
	private int getHorizontalOffset(boolean forceNormal){
		int hoffset = 0;
		
        final int gravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;

        Layout l = getLayout();
        
        if (!forceNormal && getText().length() == 0 && getHintLayout() != null) {
            l = getHintLayout();
        }

        if (gravity != Gravity.CENTER) {
            int boxw;

            if (l == getHintLayout()) {
                boxw = getMeasuredWidth() - getCompoundPaddingLeft() -
                        getCompoundPaddingRight();
            } else {
                boxw = getMeasuredWidth() - getCompoundPaddingLeft() -
                        getCompoundPaddingRight();
            }
            int textw = l.getHeight();

            if (textw < boxw) {
                if (gravity == Gravity.RIGHT)
                   hoffset = boxw - textw;
                else // (gravity == Gravity.CENTER_VERTICAL)
                    hoffset = (boxw - textw) >> 1;
            }
        }
        return hoffset;
	}

	private int getVerticalOffset(boolean b) {

		Method method = null;

		try {
			method = MongolianTextView.class.getSuperclass().getDeclaredMethod(
					"getVerticalOffset", new Class[] { boolean.class });
			method.setAccessible(true);
		} catch (SecurityException | NoSuchMethodException e) {

			e.printStackTrace();
		}
        if (method != null)
			try {
				return (Integer) method.invoke(this, new Object[] { b });
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {

				e.printStackTrace();
			}
        return 0;
	}

	private Layout getHintLayout() {
		return (Layout) getPrivateField("mHintLayout");
	}

	private void assumeLayout() {
		Method method = null;

		try {
			method = MongolianTextView.class.getSuperclass().getDeclaredMethod(
					"assumeLayout", new Class[] {});
			method.setAccessible(true);
		} catch (SecurityException | NoSuchMethodException e) {

			e.printStackTrace();
		}
        if (method != null)
			try {
				method.invoke(this, new Object[] {});
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
    }

	private Paint get_mHighlightPaint() {
		return (Paint) getPrivateField("mHighlightPaint");
	}
	
	@TargetApi(17)
	public int getOffsetForPosition(float x, float y) {
        if (getLayout() == null) return -1;
        
        final int line = getLineAtCoordinate(x);
        final int offset = getOffsetAtCoordinate(line,y);
        
        return offset;
    }
	public int getLineAtCoordinate(float x) {
        x -= getTotalPaddingLeft();
        // Clamp the position to inside of the view.
        x = Math.max(0.0f, x);
        x = Math.min(getWidth() - getTotalPaddingRight() - 1, x);
        x += getScrollX();
        return ((MongolianDynamicLayout)getLayout()).getLineForHorizontal((int)x);
    }
    private int getOffsetAtCoordinate(int line, float y) {
    	y -= getTotalPaddingTop();
        // Clamp the position to inside of the view.
        y = Math.max(0.0f, y);
        y = Math.min(getHeight() - getTotalPaddingBottom() - 1,y);
        y += getScrollY();
        return ((MongolianDynamicLayout)getLayout()).getOffsetForVertical(line, y) ;
    }
	private int get_mPreDrawState() {
        return (int)getPrivateField("mPreDrawState");
	}

    static HashMap<String,Field> privateFields = new HashMap<String, Field>();
	private Object getPrivateField(String FieldName) {
		Field field;
		try {
            if(privateFields.containsKey(FieldName)){
                field = privateFields.get(FieldName);
            }
            else{
                field = MongolianTextView.class.getSuperclass().getDeclaredField(
                        FieldName);
                field.setAccessible(true);
                privateFields.put(FieldName,field);
            }
			return field.get(this);
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		}
		return null;

	}

    Drawables dr;

	int count = 0;
	long sumTime = 0;
	private long mLastScroll;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

        if(isInEditMode()){
            super.onDraw(canvas);
        }
		final int compoundPaddingLeft = getCompoundPaddingLeft();
		final int compoundPaddingTop = getCompoundPaddingTop();
		final int compoundPaddingRight = getCompoundPaddingRight();
		final int compoundPaddingBottom = getCompoundPaddingBottom();
		final int scrollX = getScrollX();
		final int scrollY = getScrollY();
		final int right = getRight();
		final int left = getLeft();
		final int bottom = getBottom();
		final int top = getTop();

		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		int mPaddingTop = getPaddingTop();
		int mPaddingBottom = getPaddingBottom();

		Paint mHighlightPaint = get_mHighlightPaint();

		if(getLayout()==null) return;
		TextPaint mTextPaint = getLayout().getPaint();
		Layout mLayout = getLayout();
		CharSequence mText = getText();
		CharSequence mHint = getHint();
		ColorStateList mHintTextColor = getHintTextColors();
		int mCurHintTextColor = getCurrentHintTextColor();

        int mHighlightColor = get_mHighlightColor();
		
		int mGravity = getGravity();

		// 閿熸枻鎷烽敓锟絛raw閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
		if (dr == null)
			dr = new Drawables(getPrivateField("mDrawables"));
		if (dr != null) {

			int vspace = bottom - top - compoundPaddingBottom
					- compoundPaddingTop;
			int hspace = right - left - compoundPaddingRight
					- compoundPaddingLeft;

			// IMPORTANT: The coordinates computed are also used in
			// invalidateDrawable()
			// Make sure to update invalidateDrawable() when changing this code.
			if (dr.mDrawableLeft != null) {
				canvas.save();
				canvas.translate(scrollX + mPaddingLeft, scrollY
						+ compoundPaddingTop
						+ (vspace - dr.mDrawableHeightLeft) / 2);
				dr.mDrawableLeft.draw(canvas);
				canvas.restore();
			}

			// IMPORTANT: The coordinates computed are also used in
			// invalidateDrawable()
			// Make sure to update invalidateDrawable() when changing this code.
			if (dr.mDrawableRight != null) {
				canvas.save();
				canvas.translate(scrollX + right - left - mPaddingRight
						- dr.mDrawableSizeRight, scrollY + compoundPaddingTop
						+ (vspace - dr.mDrawableHeightRight) / 2);
				dr.mDrawableRight.draw(canvas);
				canvas.restore();
			}

			// IMPORTANT: The coordinates computed are also used in
			// invalidateDrawable()
			// Make sure to update invalidateDrawable() when changing this code.
			if (dr.mDrawableTop != null) {
				canvas.save();
				canvas.translate(scrollX + compoundPaddingLeft
						+ (hspace - dr.mDrawableWidthTop) / 2, scrollY
						+ mPaddingTop);
				dr.mDrawableTop.draw(canvas);
				canvas.restore();
			}

			// IMPORTANT: The coordinates computed are also used in
			// invalidateDrawable()
			// Make sure to update invalidateDrawable() when changing this code.
			if (dr.mDrawableBottom != null) {
				canvas.save();
				canvas.translate(scrollX + compoundPaddingLeft
						+ (hspace - dr.mDrawableWidthBottom) / 2, scrollY
						+ bottom - top - mPaddingBottom
						- dr.mDrawableSizeBottom);
				dr.mDrawableBottom.draw(canvas);
				canvas.restore();
			}
		}

		if(VERSION.SDK_INT<16){
			int mPreDrawState = get_mPreDrawState();
			if (mPreDrawState == PREDRAW_DONE) {
				final ViewTreeObserver observer = getViewTreeObserver();
				if (observer != null) {
					observer.removeOnPreDrawListener(this);
					SetValue("mPreDrawState", PREDRAW_NOT_REGISTERED);// mPreDrawState
																		// =
																		// PREDRAW_NOT_REGISTERED;
				}
			}
		}
		else{
			mTextPaint.drawableState = getDrawableState();
		}

		int color = (Integer) getPrivateField("mCurTextColor");

		if (mLayout == null) {
			assumeLayout();
			mLayout = getLayout();
			Log.d(TAG,mLayout.toString());
			
		}

		
		Layout layout = mLayout;

		Layout mHintLayout = getHintLayout();

		if (mHint != null && mText.length() == 0) {
			if (mHintTextColor != null) {
				color = mCurHintTextColor;
			}
			layout = mHintLayout;
		}

		mTextPaint.setColor(color);
		mTextPaint.drawableState = getDrawableState();

		canvas.save();


		int extendedPaddingTop = getExtendedPaddingTop();
		int extendedPaddingBottom = getExtendedPaddingBottom();

		float clipLeft = compoundPaddingLeft + scrollX;
		float clipTop = extendedPaddingTop + scrollY;
		float clipRight = right - left - compoundPaddingRight + scrollX;
		float clipBottom = bottom - top - extendedPaddingBottom + scrollY;

		canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);

		int voffsetText = 0;
		int voffsetCursor = 0;
		
		int hoffsetText = 0;

		// translate in by our padding
		{

			if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
				voffsetText = getVerticalOffset(false);
				voffsetCursor = getVerticalOffset(true);
				
				hoffsetText = getHorizontalOffset(false);
			}

			canvas.translate(compoundPaddingLeft + hoffsetText, extendedPaddingTop );
		}
		

		Path highlight = getUpdatedHighlightPath();

		layout.draw(canvas, highlight, mHighlightPaint, voffsetCursor
				- voffsetText);
		//Log.d("MSL",(System.currentTimeMillis() - cms)+"");
		canvas.restore();

	}

    private Path getUpdatedHighlightPath() {
        Path highlight = null;
        Paint highlightPaint = get_mHighlightPaint();

        final int selStart = getSelectionStart();
        final int selEnd = getSelectionEnd();
        if (getMovementMethod() != null && (isFocused() || isPressed()) && selStart >= 0) {
            if (selStart == selEnd) {
//                if (mEditor != null && mEditor.isCursorVisible() &&
//                        (SystemClock.uptimeMillis() - mEditor.mShowCursor) %
//                                (2 * Editor.BLINK) < Editor.BLINK) {
//                    if (mHighlightPathBogus) {
//                        if (mHighlightPath == null) mHighlightPath = new Path();
//                        mHighlightPath.reset();
//                        mLayout.getCursorPath(selStart, mHighlightPath, mText);
//                        mEditor.updateCursorsPositions();
//                        mHighlightPathBogus = false;
//                    }
//
//                    // XXX should pass to skin instead of drawing directly
//                    highlightPaint.setColor(mCurTextColor);
//                    highlightPaint.setStyle(Paint.Style.STROKE);
//                    highlight = mHighlightPath;
//                }
            } else {
                Path mHighlightPath = get_mHighlightPath();
                if (get_mHighlightPathBogus()) {
                    if (mHighlightPath == null) mHighlightPath = new Path();
                    mHighlightPath.reset();
                    getLayout().getSelectionPath(selStart, selEnd, mHighlightPath);
                    //set_mHighlightPathBogus(false);
                }

                // XXX should pass to skin instead of drawing directly
                highlightPaint.setColor(get_mHighlightColor());
                highlightPaint.setStyle(Paint.Style.FILL);

                highlight = mHighlightPath;
            }
        }
        return highlight;
    }

    Field field_mHighLightColor;
    private int get_mHighlightColor() {
        if(field_mHighLightColor==null){
            try {
                field_mHighLightColor = MongolianTextView.class.getSuperclass().getDeclaredField(
                        "mHighlightColor");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            field_mHighLightColor.setAccessible(true);

        }
        try {
            return (int)field_mHighLightColor.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Color.BLUE;
    }

    private void set_mHighlightPath(Path mHighlightPath) {
		SetValue("mHighlightPath", mHighlightPath);
	}


    private boolean get_mHighlightPathBogus() {
        return (Boolean) getPrivateField("mHighlightPathBogus");
    }

	private void set_mHighlightPathBogus(boolean b) {
		SetValue("mHighlightPathBogus", b);
	}

	private long get_mShowCursor() {
		// TODO Auto-generated method stub
		if(VERSION.SDK_INT<16)
			return (Long) getPrivateField("mShowCursor");
		try {
			Field field_mShowCursor;
			Class class_Editor = Class.forName("android.widget.Editor");
			Field field_mEditor;
			field_mShowCursor = class_Editor.getDeclaredField(
					"mShowCursor");
			field_mShowCursor.setAccessible(true);
			
			field_mEditor =TextView.class.getDeclaredField("mEditor");
			field_mEditor.setAccessible(true);
			Object editor = field_mEditor.get(this);
			if(editor==null)
				return 0;
			return field_mShowCursor.getLong(editor);
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private Path get_mHighlightPath() {
		// TODO Auto-generated method stub
		return (Path) getPrivateField("mHighlightPath");
	}

	private boolean get_mCursorVisible() {
		
			

		if(VERSION.SDK_INT<16){
			return (Boolean) getPrivateField("mCursorVisible");
		}
		try {
			Field field_mShowCursor;
			Class class_Editor = Class.forName("android.widget.Editor");
			Field field_mEditor;
			field_mShowCursor = class_Editor.getDeclaredField(
					"mCursorVisible");
			field_mShowCursor.setAccessible(true);
			
			field_mEditor =TextView.class.getDeclaredField("mEditor");
			field_mEditor.setAccessible(true);
			Object editor = field_mEditor.get(this);
			if(editor==null)
				return false;
			return field_mShowCursor.getBoolean(editor);
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void setInsertAndSelectiondisable(){
		if(VERSION.SDK_INT<16){
			SetValue("mInsertionControllerEnabled", false);
			SetValue("mSelectionControllerEnabled", false);
			return;
		}
		try {
			Field field_mInsertionControllerEnabled;
			Field field_mSelectionControllerEnabled;
			Class class_Editor = Class.forName("android.widget.Editor");
			Field field_mEditor;
			field_mInsertionControllerEnabled = class_Editor.getDeclaredField(
					"mInsertionControllerEnabled");
			field_mSelectionControllerEnabled = class_Editor.getDeclaredField(
					"mSelectionControllerEnabled");
			field_mInsertionControllerEnabled.setAccessible(true);
			field_mSelectionControllerEnabled.setAccessible(true);
			field_mEditor =TextView.class.getDeclaredField("mEditor");
			field_mEditor.setAccessible(true);
			Object editor = field_mEditor.get(this);
			if(editor==null)
				return ;
			field_mInsertionControllerEnabled.set(editor,false);
			field_mSelectionControllerEnabled.set(editor, false);
		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchFieldException e) {
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressLint("FloatMath")
	@Override
	public boolean bringPointIntoView(int offset) {
		// TODO Auto-generated method stub
		boolean changed = false;

		Layout mLayout = getLayout();
        int line = mLayout.getLineForOffset(offset);

        // FIXME: Is it okay to truncate this, or should we round?
        final int y = (int)mLayout.getPrimaryHorizontal(offset);
        
        final int left = mLayout.getLineTop(line);
        final int right = mLayout.getLineTop(line+1);

        int top = (int) Math.floor(mLayout.getLineLeft(line));
        int bottom = (int) Math.ceil(mLayout.getLineRight(line));
        
        int wd = mLayout.getHeight();

        int grav;

        switch (mLayout.getParagraphAlignment(line)) {
            case ALIGN_NORMAL:
                grav = 1;
                break;

            case ALIGN_OPPOSITE:
                grav = -1;
                break;

            default:
                grav = 0;
        }

        grav *= mLayout.getParagraphDirection(line);

        int mRight = getRight();
		int mLeft = getLeft();
		int mBottom = getBottom();
		int mTop = getTop();

        int hspace = mRight - mLeft - getCompoundPaddingLeft() - getCompoundPaddingRight();
        int vspace = mBottom - mTop - getExtendedPaddingTop() - getExtendedPaddingBottom();

        int vslack = (right - left) / 2;
        int hslack = vslack;

        if (vslack > hspace / 4)
            vslack = hspace / 4;
        if (hslack > vspace / 4)
            hslack = vspace / 4;

        int hs = getScrollX();
        int vs = getScrollY();

        if (left - hs < vslack)
            hs = left - vslack;
        if (right - hs > hspace - vslack)
            hs = right - (hspace - vslack);
        if (wd - hs < hspace)
            hs = wd - hspace;
        if (0 - hs > 0)
            hs = 0;

//        if (grav != 0) {
//            if (y - hs < hslack) {
//                hs = y - hslack;
//            }
//            if (y - hs > hspace - hslack) {
//                hs = y - (hspace - hslack);
//            }
//        }

//        if (grav < 0) {
//            if (left - hs > 0)
//                hs = left;
//            if (right - hs < hspace)
//                hs = right - hspace;
//        } else if (grav > 0) {
//            if (right - hs < hspace)
//                hs = right - hspace;
//            if (left - hs > 0)
//                hs = left;
//        } else /* grav == 0 */ {
//            if (right - left <= hspace) {
//                /*
//                 * If the entire text fits, center it exactly.
//                 */
//                hs = left - (hspace - (right - left)) / 2;
//            } else if (x > right - hslack) {
//                /*
//                 * If we are near the right edge, keep the right edge
//                 * at the edge of the view.
//                 */
//                hs = right - hspace;
//            } else if (x < left + hslack) {
//                /*
//                 * If we are near the left edge, keep the left edge
//                 * at the edge of the view.
//                 */
//                hs = left;
//            } else if (left > hs) {
//                /*
//                 * Is there whitespace visible at the left?  Fix it if so.
//                 */
//                hs = left;
//            } else if (right < hs + hspace) {
//                /*
//                 * Is there whitespace visible at the right?  Fix it if so.
//                 */
//                hs = right - hspace;
//            } else {
//                /*
//                 * Otherwise, float as needed.
//                 */
//                if (x - hs < hslack) {
//                    hs = x - hslack;
//                }
//                if (x - hs > hspace - hslack) {
//                    hs = x - (hspace - hslack);
//                }
//            }
//        }

        Scroller mScroller  = get_mScroller(); 

        if (hs != getScrollX() || vs != getScrollY()) {
            if (mScroller == null) {
                scrollTo(hs, vs);
            } else {
                long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
                int dx = hs - getScrollX();
                int dy = vs - getScrollY();

                if (duration > ANIMATED_SCROLL_GAP) {
                    mScroller.startScroll(getScrollX(), getScrollY(), dx, dy);
                    awakenScrollBars(mScroller.getDuration());
                    invalidate();
                } else {
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }

                    scrollBy(dx, dy);
                }

                mLastScroll = AnimationUtils.currentAnimationTimeMillis();
            }

            changed = true;
        }

        if (isFocused()) {
            // This offsets because getInterestingRect() is in terms of
            // viewport coordinates, but requestRectangleOnScreen()
            // is in terms of content coordinates.

            Rect r = new Rect();
            getInterestingRect(r, y, top, bottom, line);
            r.offset(getScrollX(),getScrollY());

            if (requestRectangleOnScreen(r)) {
                changed = true;
            }
        }

        return changed;
	}

	private void getInterestingRect(Rect r, int h, int top, int bottom,
            int line) {
		int paddingTop = getExtendedPaddingTop();
        if ((getGravity() & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
            paddingTop += getVerticalOffset(false);
        }
        top += paddingTop;
        bottom += paddingTop;
        h += getCompoundPaddingLeft();

        if (line == 0)
            top -= getExtendedPaddingTop();
        if (line == getLayout().getLineCount() - 1)
            bottom += getExtendedPaddingBottom();

        r.set(h, top, h+1, bottom);
        r.offset(-getScrollX(), -getScrollY());
	}

	private Scroller get_mScroller() {
		// TODO Auto-generated method stub
		return (Scroller)getPrivateField("mScroller");
	}


//	@Override
//	public void invalidate(int l, int t, int r, int b) {
//		// TODO Auto-generated method stub
//		super.invalidate();
//	}

	int measuringCount = 1;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//Log.d(TAG,"OnMeasuring" + measuringCount ++ );

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		BoringLayout.Metrics boring = UNKNOWN_BORING;
		BoringLayout.Metrics hintBoring = UNKNOWN_BORING;

		int des = -1;
		boolean fromexisting = false;

		// ################################
		Layout mLayout = getLayout();
		Layout mHintLayout = (Layout) getPrivateField("mHintLayout");

		TextUtils.TruncateAt mEllipsize = getEllipsize();
		CharSequence mHint = (CharSequence) getPrivateField("mHint");
		CharSequence mTransformed = (CharSequence) getPrivateField("mTransformed");

		TextPaint mTextPaint = (TextPaint) getPrivateField("mTextPaint");
		BoringLayout.Metrics mBoring = (BoringLayout.Metrics) getPrivateField("mBoring");
		BoringLayout.Metrics mHintBoring = (BoringLayout.Metrics) getPrivateField("mHintBoring");
		// ################################



		if (heightMode == MeasureSpec.EXACTLY) {
			// Parent has told us how big to be. So be it.
			height = heightSize;
			SetValue("mDesiredHeightAtMeasure", -1);// mDesiredHeightAtMeasure =
			// -1;
		} else {
			if (mLayout != null && mEllipsize == null) {
				des = desired(mLayout);
			}

			if (des < 0) {
				boring = BoringLayout.isBoring(mTransformed, mTextPaint,
						mBoring);
				if (boring != null) {
					SetValue("mBoring", boring);// mBoring = boring;
				}
			} else {
				fromexisting = true;
			}

			if (boring == null || boring == UNKNOWN_BORING) {
				if (des < 0) {
					des = (int) Math.ceil(Layout.getDesiredWidth(
							mTransformed, mTextPaint));

					if(getParent() instanceof RelativeLayout){
						ViewGroup.LayoutParams layoutParams = getLayoutParams();
						int myHeight = layoutParams.height;
						if(myHeight!= ViewGroup.LayoutParams.WRAP_CONTENT&&myHeight!= ViewGroup.LayoutParams.MATCH_PARENT)
						{
							des = myHeight;
						}
					}
				}

				height = des;
			} else {
				height = boring.width;
			}

			if (dr == null)
				dr = new Drawables(getPrivateField("mDrawables"));
			if (dr != null) {
				height = Math.max(height, getCompoundPaddingTop());
				height = Math.max(height, getCompoundPaddingBottom());
			}

			if (mHint != null) {
				int hintDes = -1;
				int hintWidth;

				if (mHintLayout != null && mEllipsize == null) {
					hintDes = desired(mHintLayout);
				}

				if (hintDes < 0) {
					hintBoring = BoringLayout.isBoring(mHint, mTextPaint,
							mHintBoring);
					if (hintBoring != null) {
						SetValue("mHintBoring", hintBoring);// mHintBoring =
						// hintBoring;
					}
				}

				if (hintBoring == null || hintBoring == UNKNOWN_BORING) {
					if (hintDes < 0) {
						hintDes = (int) Math.ceil(Layout.getDesiredWidth(
								mHint, mTextPaint));
					}

					hintWidth = hintDes;
				} else {
					hintWidth = hintBoring.width;
				}

				if (hintWidth > height) {
					height = hintWidth;
				}
			}

			height += getCompoundPaddingTop() + getCompoundPaddingBottom();

			if (mMaxWidthMode == EMS) {
				height = Math.min(height, mMaxWidth * getLineHeight());
			} else {
				height = Math.min(height, mMaxWidth);
			}

			if (mMinWidthMode == EMS) {
				height = Math.max(height, mMinWidth * getLineHeight());
			} else {
				height = Math.max(height, mMinWidth);
			}

			// Check against our minimum width
			height = Math.max(height, getSuggestedMinimumWidth());

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(heightSize, height);
			}
		}

		int want = height - getCompoundPaddingTop()
				- getCompoundPaddingBottom();

		int hintWant = want;

		// ####################
		boolean mHorizontallyScrolling = (Boolean) getPrivateField("mHorizontallyScrolling");
		// #####################
		if (mHorizontallyScrolling)
			want = VERY_WIDE;

		int hintWidth = mHintLayout == null ? hintWant : mHintLayout.getWidth();

		if (mLayout == null) {
			makeNewLayoutM(want, hintWant, null, null, height
							- getCompoundPaddingTop() - getCompoundPaddingBottom(),
					false);
		} else if ((mLayout.getWidth() != want) || (hintWidth != hintWant)
				|| (mLayout.getEllipsizedWidth() != want)) {
			if (mHint == null
					&& mEllipsize == null
					&& want > mLayout.getWidth()
					&& (mLayout instanceof BoringLayout || (fromexisting
					&& des >= 0 && des <= want))) {
				mLayout.increaseWidthTo(want);
			} else {
				makeNewLayoutM(want, hintWant, null, null, height
								- getCompoundPaddingTop() - getCompoundPaddingBottom(),
						false);
			}
		} else {
			// Width has not changed.
		}

		mLayout = getLayout();

		if (widthMode == MeasureSpec.EXACTLY) {
			// Parent has told us how big to be. So be it.
			width = widthSize;

		} else {
			int desired = Math.max(getDesiredHeight(mLayout, true),
					getDesiredHeight(mHintLayout, mEllipsize != null));// getDesiredHeight();

			width = desired;
			// SetValue("mDesiredHeightAtMeasure",
			// desired);//mDesiredHeightAtMeasure = desired;

			if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(desired, widthSize);
			}
		}

		int unpaddedWidth = width;
		int unpaddedHeight = want;
		if(mLayout==null) {
			setMeasuredDimension(width, height);
			return;
		}
		if (mMaxMode == LINES && mLayout.getLineCount() > mMaximum) {
			unpaddedWidth = Math.min(unpaddedWidth,
					mLayout.getLineTop(mMaximum));
		}

		/*
		 * We didn't let makeNewLayout() register to bring the cursor into view,
		 * so do it here if there is any possibility that it is needed.
		 */

		MovementMethod mMovement = getMovementMethod();
		if (mMovement != null || mLayout.getWidth() > unpaddedWidth
				|| mLayout.getHeight() > unpaddedHeight) {
			registerForPreDraw();
		} else {
			scrollTo(0, 0);
		}

		setMeasuredDimension(width, height);
	}

	public void makeNewLayoutM(int w, int hintWidth,
							  BoringLayout.Metrics boring, BoringLayout.Metrics hintBoring,
							  int ellipsisWidth, boolean bringIntoView) {
		Method method = null;

		try {
			method = MongolianTextView.class.getSuperclass()
					.getDeclaredMethod(
							"makeNewLayout",
							new Class[] { int.class, int.class,
									BoringLayout.Metrics.class,
									BoringLayout.Metrics.class, int.class,
									boolean.class });
			method.setAccessible(true);
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		if (method != null)
			try {
				method.invoke(this, new Object[] { w, hintWidth, boring,
						hintBoring, ellipsisWidth, bringIntoView });
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	}

	private void registerForPreDraw() {
		// TODO Auto-generated method stub
		Method method = null;

		try {
			method = MongolianTextView.class.getSuperclass().getDeclaredMethod(
					"registerForPreDraw", new Class[] {});
			method.setAccessible(true);
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		if (method != null)
			try {
				method.invoke(this, new Object[] {});
			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (InvocationTargetException e) {

				e.printStackTrace();
			}
	}

	private static int desired(Layout layout) {
		int n = layout.getLineCount();
		CharSequence text = layout.getText();
		float max = 0;

		// if any line was wrapped, we can't use it.
		// but it's ok for the last line not to have a newline

		for (int i = 0; i < n - 1; i++) {
			if (text.charAt(layout.getLineEnd(i) - 1) != '\n')
				return -1;
		}

		for (int i = 0; i < n; i++) {
			max = Math.max(max, layout.getLineWidth(i));
		}

		return (int) Math.ceil(max);
	}

	private int getDesiredHeight(Layout layout, boolean cap) {
		if (layout == null) {
			return 0;
		}
		int linecount = layout.getLineCount();
		int pad = getCompoundPaddingLeft() + getCompoundPaddingRight();
		// Log.e(""+pad, ""+layout.getWidth());
		int desired = layout.getLineTop(linecount);

		if (dr == null)
			dr = new Drawables(getPrivateField("mDrawables"));
		if (dr != null) {
			desired = Math.max(desired, getCompoundPaddingLeft());
			desired = Math.max(desired, getCompoundPaddingRight());
		}
		desired += pad;

		if (mMaxMode == LINES) {
			/*
			 * Don't cap the `hint to a certain number of lines. (Do cap it,
			 * though, if we have a maximum pixel height.)
			 */
			if (cap) {
				if (linecount > mMaximum) {
					desired = layout.getLineTop(mMaximum)
							+ layout.getBottomPadding();
					desired += pad;
					linecount = mMaximum;
				}
			}
		} else {
			desired = Math.min(desired, mMaximum);
		}

		if (mMinMode == LINES) {
			if (linecount < mMinimum) {
				desired += getLineHeight() * (mMinimum - linecount);
			}
		} else {
			desired = Math.max(desired, mMinimum);
		}

		// Check against our minimum height
		// desired = Math.max(desired, getSuggestedMinimumWidth());

		return desired;
	}

	class Drawables {
		Rect mCompoundRect = new Rect();
		Drawable mDrawableTop, mDrawableBottom, mDrawableLeft, mDrawableRight;
		int mDrawableSizeTop, mDrawableSizeBottom, mDrawableSizeLeft,
				mDrawableSizeRight;
		int mDrawableWidthTop, mDrawableWidthBottom, mDrawableHeightLeft,
				mDrawableHeightRight;
		int mDrawablePadding;

		public Drawables(Object object) {
			if (object != null) {
				try {
					mCompoundRect = (Rect) GetField("mCompoundRect", object);

					mDrawableTop = (Drawable) GetField("mDrawableTop", object);
					mDrawableBottom = (Drawable) GetField("mDrawableBottom",
							object);
					mDrawableLeft = (Drawable) GetField("mDrawableLeft", object);
					mDrawableRight = (Drawable) GetField("mDrawableRight",
							object);

					mDrawableSizeTop = (Integer) GetField("mDrawableSizeTop",
							object);
					mDrawableSizeBottom = (Integer) GetField(
							"mDrawableSizeBottom", object);
					mDrawableSizeLeft = (Integer) GetField("mDrawableSizeLeft",
							object);
					mDrawableSizeRight = (Integer) GetField(
							"mDrawableSizeRight", object);

					mDrawableWidthTop = (Integer) GetField("mDrawableWidthTop",
							object);
					mDrawableWidthBottom = (Integer) GetField(
							"mDrawableWidthBottom", object);
					mDrawableHeightLeft = (Integer) GetField(
							"mDrawableHeightLeft", object);
					mDrawableHeightRight = (Integer) GetField(
							"mDrawableHeightRight", object);

					mDrawablePadding = (Integer) GetField("mDrawablePadding",
							object);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Object GetField(String name, Object object) throws SecurityException,
				NoSuchFieldException, IllegalArgumentException,
				IllegalAccessException {
			Class mClass = object.getClass();
			Field field = mClass.getDeclaredField(name);
			field.setAccessible(true);
			return field.get(object);
		}
	}

	class InputMethodState {
		Rect mCursorRectInWindow = new Rect();
		RectF mTmpRectF = new RectF();
		float[] mTmpOffset = new float[2];
		ExtractedTextRequest mExtracting;
		final ExtractedText mTmpExtracted = new ExtractedText();
		int mBatchEditNesting;
		boolean mCursorChanged;
		boolean mSelectionModeChanged;
		boolean mContentChanged;
		int mChangedStart, mChangedEnd, mChangedDelta;

		Object GetField(String name, Object object) throws SecurityException,
				NoSuchFieldException, IllegalArgumentException,
				IllegalAccessException {
			Class mClass = object.getClass();
			Field field = mClass.getDeclaredField(name);
			field.setAccessible(true);
			return field.get(object);
		}

		public InputMethodState(Object object) {
			try {
				if (object != null) {

					this.mCursorRectInWindow = (Rect) GetField(
							"mCursorRectInWindow", object);
					this.mTmpRectF = (RectF) GetField("mTmpRectF", object);
					this.mTmpOffset = (float[]) GetField("mTmpOffset", object);
					this.mExtracting = (ExtractedTextRequest) GetField(
							"mExtracting", object);
					this.mBatchEditNesting = (Integer) GetField(
							"mBatchEditNesting", object);
					this.mCursorChanged = (Boolean) GetField("mCursorChanged",
							object);
					this.mSelectionModeChanged = (Boolean) GetField(
							"mSelectionModeChanged", object);
					this.mContentChanged = (Boolean) GetField(
							"mContentChanged", object);
					this.mChangedDelta = (Integer) GetField("mChangedDelta",
							object);
					this.mChangedEnd = (Integer) GetField("mChangedEnd", object);
					this.mChangedStart = (Integer) GetField("mChangedStart",
							object);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onPreDraw() {
		setInsertAndSelectiondisable();

        Layout layout = getLayout();
        if (layout instanceof StaticLayout
				&& !(layout instanceof MongolianStaticLayout)) {
			layout = new MongolianStaticLayout(layout.getText(),
					layout.getPaint(), layout.getWidth(),
					layout.getAlignment(), layout.getSpacingMultiplier(),
					layout.getSpacingAdd(), false);
            setLayout(layout);
		} else if (layout instanceof DynamicLayout
				&& !(layout instanceof MongolianDynamicLayout)) {

			layout = new MongolianDynamicLayout(layout.getText(),
					layout.getText(), layout.getPaint(), layout.getWidth(),
					Layout.Alignment.ALIGN_NORMAL, layout.getSpacingMultiplier(),
					layout.getSpacingAdd(), false);
            setLayout(layout);
        }
		return super.onPreDraw();
	}

	class CommitSelectionReceiver extends ResultReceiver {
		int mNewStart;
		int mNewEnd;

		CommitSelectionReceiver() {
			super(getHandler());
		}

		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode != InputMethodManager.RESULT_SHOWN) {
				final int len = getText().length();
				if (mNewStart > len) {
					mNewStart = len;
				}
				if (mNewEnd > len) {
					mNewEnd = len;
				}
				Selection.setSelection((Spannable) getText(), mNewStart,
						mNewEnd);
			}
		}
	}
	
	
}