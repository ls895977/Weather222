package com.menksoft.android.text;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;

import com.menksoft.android.text.utils.EmojiFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.android.internal.util.ArrayUtils;
public class MongolianStaticLayout extends android.text.StaticLayout {

	private static Rect sTempRect = new Rect();
	
	private static final ParagraphStyle[] NO_PARA_SPANS  = (ParagraphStyle[]) Array.newInstance(ParagraphStyle.class, 0);

	private RectF mEmojiRect;

	private int MIN_EMOJI = -1;

	private int MAX_EMOJI  = -1;

	private Method method_getLineMax;
	private Method method_getLineMaxHighAPI;

	private Field field_mDirectionsShort;
	//private Field field_mDirections;
    private TextPaint mWorkPaint;
    private Field field_mDirections;


    public MongolianStaticLayout(CharSequence source, TextPaint paint,
			int width, Alignment align, float spacingmult, float spacingadd,
			boolean includepad) {
		super(source, paint, width, align, spacingmult, spacingadd, includepad);
        init();
	}
	
	public MongolianStaticLayout(CharSequence source, int bufstart, int bufend,
			TextPaint paint, int outerwidth, Alignment align,
			float spacingmult, float spacingadd, boolean includepad) {
		super(source, bufstart, bufend, paint, outerwidth, align, spacingmult,
				spacingadd, includepad);
        init();
	}

	public MongolianStaticLayout(CharSequence source, int bufstart, int bufend,
            TextPaint paint, int outerwidth,
            Alignment align,
            float spacingmult, float spacingadd,
            boolean includepad,
            TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
		super(
				source, 
				bufstart, bufend, 
				paint, 
				outerwidth, 
				align, 
				spacingmult, spacingadd, 
				includepad,
				ellipsize, ellipsizedWidth);
        init();
	}
	
	void init(){
        mWorkPaint = new TextPaint();
    }

	@SuppressLint("NewApi")
	@Override
	public void draw(Canvas c, Path highlight, Paint highlightPaint,
            int cursorOffsetVertical) {


		int dtop, dbottom;

		TextPaint mPaint = getPaint();
		CharSequence mText = getText();
		int mWidth = getWidth();
		boolean mSpannedText = isSpanned();

		synchronized (sTempRect) {
            if (!c.getClipBounds(sTempRect)) {
                return;
            }

            dtop = sTempRect.left;
            dbottom = sTempRect.right;
        }


        int top = 0;
        int lineCount = getLineCount();
        int bottom =  getLineTop(lineCount);

        if (dtop > top) {
            top = dtop;
        }
        if (dbottom < bottom) {
            bottom = dbottom;
        }

        int first = getLineForVertical(top);
        int last = getLineForVertical(bottom);

        int previousLineBottom = -getLineTop(first);
        int previousLineEnd = getLineStart(first);


		TextPaint paint = mPaint;

		CharSequence buf = mText;
        int width = mWidth;
        boolean spannedText = mSpannedText;

        ParagraphStyle[] spans = NO_PARA_SPANS;
        int spanend = 0;
        int textLength = 0;

        // First, draw LineBackgroundSpans.
        // LineBackgroundSpans know nothing about the alignment or direction of
        // the layout or line.  XXX: Should they?
        if (spannedText) {
            textLength = buf.length();
            for (int i = first; i <= last; i++) {
                int start = previousLineEnd;
                int end = getLineStart(i+1);
                previousLineEnd = end;

                int ltop = previousLineBottom;
                int lbottom =- getLineTop(i+1);
                previousLineBottom = lbottom;
                int lbaseline = lbottom + getLineDescent(i);

                if (start >= spanend) {
                   Spanned sp = (Spanned) buf;
                   spanend = sp.nextSpanTransition(start, textLength,
                                                   LineBackgroundSpan.class);
                   spans = sp.getSpans(start, spanend,
                                       LineBackgroundSpan.class);
                }

                for (int n = 0; n < spans.length; n++) {
                    LineBackgroundSpan back = (LineBackgroundSpan) spans[n];

                    back.drawBackground(c, paint, 0, width,
                                       ltop, lbaseline, lbottom,
                                       buf, start, end,
                                       i);
                }
            }
            // reset to their original values
            spanend = 0;
            previousLineBottom = -getLineTop(first);
            previousLineEnd = getLineStart(first);
            spans = NO_PARA_SPANS;
        }

        // There can be a highlight even without spans if we are drawing
        // a non-spanned transformation of a spanned editing buffer.
        if (highlight != null) {
            if (cursorOffsetVertical != 0) {
                c.translate(0, cursorOffsetVertical);
            }

            c.drawPath(highlight, highlightPaint);

            if (cursorOffsetVertical != 0) {
                c.translate(0, -cursorOffsetVertical);
            }
        }

        Alignment align = getAlignment();

        // Next draw the lines, one at a time.
        // the baseline is the top of the following line minus the current
        // line's descent.


        c.save();
        c.rotate(90.0f);

        PorterDuffXfermode clearPorterDuffXfermode = new PorterDuffXfermode(Mode.CLEAR);
		Paint clearPaint = new Paint();
		clearPaint.setXfermode(clearPorterDuffXfermode);
		//Log.d("MW", mWidth+"" + top);

		//c.drawRect(0, ,sTempRect.bottom, -sTempRect.right,paint);

		c.saveLayerAlpha(0, -sTempRect.right,sTempRect.bottom, top, 255, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);

        for (int i = first; i <= last; i++) {
            int start = previousLineEnd;

            previousLineEnd = getLineStart(i+1);
            int end = getLineVisibleEnd(i, start, previousLineEnd);

            int ltop = previousLineBottom;
            int lbottom = -getLineTop(i+1);
            previousLineBottom = lbottom;
            int lbaseline = ltop - getLineDescent(i);

            boolean isFirstParaLine = false;
            if (spannedText) {
                if (start == 0 || buf.charAt(start - 1) == '\n') {
                    isFirstParaLine = true;
                }
                // New batch of paragraph styles, compute the alignment.
                // Last alignment style wins.
                if (start >= spanend) {
                    Spanned sp = (Spanned) buf;
                    spanend = sp.nextSpanTransition(start, textLength,
                                                    ParagraphStyle.class);
                    spans = sp.getSpans(start, spanend, ParagraphStyle.class);

                    align = getAlignment();
                    for (int n = spans.length-1; n >= 0; n--) {
                        if (spans[n] instanceof AlignmentSpan) {
                            align = ((AlignmentSpan) spans[n]).getAlignment();
                            break;
                        }
                    }
                }
            }

            int dir = getParagraphDirection(i);
            int left = 0;
            int right = mWidth;

            // Draw all leading margin spans.  Adjust left or right according
            // to the paragraph direction of the line.
            if (spannedText) {
                final int length = spans.length;
                for (int n = 0; n < length; n++) {
                    if (spans[n] instanceof LeadingMarginSpan) {
                        LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];

                        if (dir == DIR_RIGHT_TO_LEFT) {
                            margin.drawLeadingMargin(c, paint, right, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);

                            right -= margin.getLeadingMargin(isFirstParaLine);
                        } else {
                            margin.drawLeadingMargin(c, paint, left, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);

                            boolean useMargin = isFirstParaLine;
                            if (margin instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                                int count = ((LeadingMarginSpan.LeadingMarginSpan2)margin).getLeadingMarginLineCount();
                                useMargin = count > i;
                            }
                            left += margin.getLeadingMargin(useMargin);
                        }
                    }
                }
            }

            // Adjust the point at which to start rendering depending on the
            // alignment of the paragraph.
            int x;
            if (align == Alignment.ALIGN_NORMAL) {
                if (dir == DIR_LEFT_TO_RIGHT) {
                    x = left;
                } else {
                    x = right;
                }
            } else {
                int max ;
                if(VERSION.SDK_INT>=15)
                {
                	max = (int)getLineMaxHighAPI(i);
                }
                else {
                	max = (int)getLineMax(i, spans, false);
				}
                if (align == Alignment.ALIGN_OPPOSITE) {
                    if (dir == DIR_RIGHT_TO_LEFT) {
                        x = left + max;
                    } else {
                        x = right - max;
                    }
                } else {
                    // Alignment.ALIGN_CENTER
                    max = max & ~1;
                    int half = (right - left - max) >> 1;
                    if (dir == DIR_RIGHT_TO_LEFT) {
                        x = right - half;
                    } else {
                        x = left + half;
                    }
                }
            }

            Directions directions = getLineDirections(i);
            boolean hasTab = getLineContainsTab(i);
            Directions DIRS_ALL_LEFT_TO_RIGHT = getDIRS_ALL_LEFT_TO_RIGHT();
            if (directions == DIRS_ALL_LEFT_TO_RIGHT &&
                    !spannedText && !hasTab) {

            	//c.saveLayerAlpha(x,lbottom,right,ltop, 255, Canvas.FULL_COLOR_LAYER_SAVE_FLAG);

            	//Log.d("Save RECT", String.format("%d %d %d %d",x,lbottom,right,ltop));

            	c.drawText(buf, start, end, x, lbaseline, paint);

            	float xWidth = paint.measureText("啊");

            	synchronized(this){

            		CharSequence inLineCharSequence = buf.subSequence(start, end);

            		Matcher matcher  = CJKPattern.matcher(inLineCharSequence);

	            	if(matcher!=null){
	            		float fontHeight= paint.getFontMetrics().bottom-paint.getFontMetrics().top;
	            		int lastIndex = -8000;
	            		float lastLeft = -8000;
	            		while (matcher.find()) {
							int idx= matcher.start();

							RectF cjkRect = new RectF();

							float xLeft = -100.f;
							if(idx > 0){
								if(idx-lastIndex ==-1)
									xLeft = lastLeft + xWidth;
								else
									xLeft = paint.measureText((String)inLineCharSequence.subSequence(0, idx));
							}
							else if (idx==0){
								xLeft = 0.0f;
							}

							//Log.d("MSL", "xWidth: " + xWidth);
							cjkRect.left = xLeft;
							cjkRect.top = lbottom;
							cjkRect.right = cjkRect.left + xWidth;
							cjkRect.bottom = cjkRect.top + fontHeight;
							c.drawRect(cjkRect, clearPaint);

							c.save();

							c.rotate(-90, (int)cjkRect.centerX(), (int)cjkRect.centerY());
							c.translate(0, paint.getFontMetrics().top - paint.getFontMetrics().ascent);
							c.drawText((String) inLineCharSequence.subSequence(idx, idx+1), xLeft,lbaseline , paint);

							c.restore();

							lastIndex = idx;
							lastLeft = xLeft;
	            		}
	            	}

            	}
            } else {
            	TextPaint mWorkPaint = getWorkingPaint();
                drawText(c, buf, start, end, dir, directions,
                    x, ltop, lbaseline, lbottom, paint, mWorkPaint ,
                    hasTab, spans);
            }
        }

        c.restore();
	}
	
	Pattern CJKPattern = Pattern.compile("\\p{InCJK_Unified_Ideographs}|"
				+ "\\p{InHiragana}|\\p{InKatakana}|" 
				+ "\\p{InHangul_Compatibility_Jamo}|"
				+ "\\p{InHangul_Jamo}|"
				+ "\\p{InHangul_Syllables}");
	
	private void drawText(Canvas canvas,
            CharSequence text, int start, int end,
            int dir, Directions directions,
            float x, int top, int y, int bottom,
            TextPaint paint,
            TextPaint workPaint,
            boolean hasTabs, Object[] parspans)
	{
		char[] buf;
        if (!hasTabs) {
            Directions DIRS_ALL_LEFT_TO_RIGHT = getDIRS_ALL_LEFT_TO_RIGHT();
			if (directions == DIRS_ALL_LEFT_TO_RIGHT) {
                Styled.drawText(canvas, text, start, end, dir, false, x, top, y, bottom, paint, workPaint, false);

                return;
            }
            buf = null;
        } else {
            buf=  TextUtils.substring(text, start, end).toCharArray();
        }


        float h = 0;

        int here = 0;

        int[] mDirections = null;
       if(VERSION.SDK_INT>=15)
    	   mDirections = getReflect_mDirections(directions);
       else {
    	   short [] temp = getReflect_mDirectionsShort(directions);
    	   mDirections = new int[temp.length];
    	   for (int i =0;i<temp.length;i++) {
    		   mDirections [i] = 0 + temp[i];
    	   }
       }
        if(null!=mDirections)
        for (int i = 0; i < mDirections.length; i++) {
            int there = here + mDirections[i];
            if (there > end - start)
                there = end - start;

            int segstart = here;
            for (int j = hasTabs ? here : there; j <= there; j++) {
                if (j == there || buf[j] == '\t') {
                    h += Styled.drawText(canvas, text,
                                         start + segstart, start + j,
                                         dir, (i & 1) != 0, x + h,
                                         top, y, bottom, paint, workPaint,
                                         start + j != end);

                    if (j != there && buf[j] == '\t')
                        h = dir * Layout_nextTab(text, start, end, h * dir, parspans);

                    segstart = j + 1;
                } else if (hasTabs && buf[j] >= 0xD800 && buf[j] <= 0xDFFF && j + 1 < there) {
                    int emoji = Character.codePointAt(buf, j);
                    if(MIN_EMOJI==-1)
                    {
                    	MIN_EMOJI =  EmojiFactory.newAvailableInstance().getMinimumAndroidPua();
               		 	MAX_EMOJI = EmojiFactory.newAvailableInstance().getMaximumAndroidPua();
                    }
                    if (emoji >= MIN_EMOJI && emoji <= MAX_EMOJI) {

                        Bitmap bm = EmojiFactory.newAvailableInstance().
                            getBitmapFromAndroidPua(emoji);

                        if (bm != null) {
                            h += Styled.drawText(canvas, text,
                                                 start + segstart, start + j,
                                                 dir, (i & 1) != 0, x + h,
                                                 top, y, bottom, paint, workPaint,
                                                 start + j != end);

                            if (mEmojiRect == null) {
                                mEmojiRect = new RectF();
                            }

                            workPaint.set(paint);
                            Styled.measureText(paint, workPaint, text,
                                               start + j, start + j + 1,
                                               null);

                            float bitmapHeight = bm.getHeight();
                            float textHeight = -workPaint.ascent();
                            float scale = textHeight / bitmapHeight;
                            float width = bm.getWidth() * scale;

                            mEmojiRect.set(x + h, y - textHeight,
                                           x + h + width, y);

                            canvas.save();

                            canvas.drawBitmap(bm, null, mEmojiRect, paint);
                            canvas.restore();
                            h += width;

                            j++;
                            segstart = j + 1;
                        }
                    }
                }
            }

            here = there;
        }

        if (hasTabs)
            TextUtils_recycle(buf);
	}
	
	private void TextUtils_recycle(char[] buf) {
		// TODO Auto-generated method stub
		
	}
	
	private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = getText();
        char ch;
        if (line == getLineCount() - 1) {
            return end;
        }

        for (; end > start; end--) {
            ch = text.charAt(end - 1);

            if (ch == '\n') {
                return end - 1;
            }

            if (ch != ' ' && ch != '\t') {
                break;
            }

        }

        return end;
    }
	
	
	@SuppressWarnings("rawtypes")
	private float getLineMax(int line, Object[] tabs, boolean full) {
		if (method_getLineMax == null) {
			try {
				Class[] parameterTypes = new Class[] { int.class, Object[].class,
						boolean.class };
				method_getLineMax = Layout.class.getDeclaredMethod(
						"getLineMax", parameterTypes);
				method_getLineMax.setAccessible(true);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		try {
			return (Float) method_getLineMax.invoke(this, line, tabs, full);
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
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	private float getLineMaxHighAPI(int line) 
	{
		if (method_getLineMaxHighAPI == null) {
			Class[] parameterTypes = new Class[] { int.class };
			try {
				method_getLineMaxHighAPI = Layout.class.getDeclaredMethod(
						"getLineMax", parameterTypes);
				method_getLineMaxHighAPI.setAccessible(true);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		try {
			return (Float) method_getLineMaxHighAPI.invoke(this, line);
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
		return 0;
	}

	private TextPaint getWorkingPaint() {

        return mWorkPaint;
//		if (field_mWorkingPaint == null) {
//			try {
//				field_mWorkingPaint = Layout.class
//						.getDeclaredField("mWorkPaint");
//				field_mWorkingPaint.setAccessible(true);
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			} catch (NoSuchFieldException e) {
//
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//
//				e.printStackTrace();
//			}
//		}
//		try {
//			return (TextPaint) field_mWorkingPaint.get(this);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return null;
	}


	/*��sdk<15Directions����������ΪShort
	 * */
	private short[] getReflect_mDirectionsShort(Directions directions) {
		if(field_mDirectionsShort==null){
			try {
				field_mDirectionsShort = Directions.class
						.getDeclaredField("mDirections");
				field_mDirectionsShort.setAccessible(true);
			} catch (SecurityException e) {

				e.printStackTrace();
			} catch (NoSuchFieldException e) {

				e.printStackTrace();
			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} 
		}
		try {
			return (short[]) field_mDirectionsShort.get(directions);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public int[] getReflect_mDirections(Directions directions)
	{
		if (field_mDirections == null) {
			try {
				field_mDirections = Directions.class
						.getDeclaredField("mDirections");
				field_mDirections.setAccessible(true);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		try {
			return (int[]) field_mDirections.get(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	@SuppressWarnings("rawtypes")
	private float Layout_nextTab(CharSequence text, int start, int end, float f,
			Object[] parspans) {
		// TODO Auto-generated method stub
		Class [] parameterTypes=new Class[]{
				CharSequence.class, int.class, int.class,
                float.class, Object[].class
					};
			try {
				Method method = Layout.class.getDeclaredMethod("nextTab",parameterTypes);
				method.setAccessible(true);
				return (Float) method.invoke(null, 
						text, start, end, f,
						parspans);
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

}
