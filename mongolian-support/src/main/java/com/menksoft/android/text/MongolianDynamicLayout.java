package com.menksoft.android.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.menksoft.android.text.utils.EmojiFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

//import com.android.internal.util.ArrayUtils;
//import android.emoji.EmojiFactory;

public class MongolianDynamicLayout extends DynamicLayout {

	/**
	 * Fields From Layout
	 * */
	private static Rect sTempRect = new Rect();
	
	private RectF mEmojiRect;

	private int MIN_EMOJI = -1;
	private TextPaint mWorkPaint = new TextPaint();

	public int getOffsetForVertical(int line, float verti) {

		int max = getLineEnd(line) - 1;
        int min = getLineStart(line);
        
        
        Directions dirs = getLineDirections(line);

        if (line == getLineCount() - 1)
            max++;
        

        int best = min;
        
        
        
        int[] mDirections = null;
        if(VERSION.SDK_INT>=15)
     	   mDirections = GetDirections_mDirections(dirs);
        else {
     	   short [] temp = GetDirections_mDirectionsShort(dirs);
     	   mDirections = new int[temp.length];
     	   for (int i =0;i<temp.length;i++) {
     		   mDirections [i] = 0 + temp[i];
     	   }
        }
        
        float bestdist = Math.abs(getPrimaryHorizontal(best) - verti);

        int here = min;
        
        for (int i = 0; i < mDirections.length; i++) {
            int there = here + mDirections[i];
            int swap =-1;// ((i & 1) == 0) ? 1 : -1;

            if (there > max)
                there = max;

            
            int high = there , low = here, guess;

            while (high - low > 1) {
                guess = (high + low) / 2;
                int adguess = getOffsetAtStartOf(guess);
                if (getPrimaryHorizontal(adguess)  >= verti )
                    high = guess;
                else
                    low = guess;
            }
            if (low < here + 1)
                low = here + 1;

            if (low < there) {
                low = getOffsetAtStartOf(low);

                float dist = Math.abs(getPrimaryHorizontal(low) - verti);

                int aft = TextUtils.getOffsetAfter(getText(), low);
                if (aft < there) {
                    float other = Math.abs(getPrimaryHorizontal(aft) - verti);

                    if (other < dist) {
                        dist = other;
                        low = aft;
                    }
                }

                if (dist < bestdist) {
                    bestdist = dist;
                    best = low;   
                }
            }

            float dist = Math.abs(getPrimaryHorizontal(here) - verti);

            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }

            here = there;
        }

        float dist = Math.abs(getPrimaryHorizontal(max) - verti);

        if (dist < bestdist) {
            bestdist = dist;
            best = max;
        }

        return best;
	}

	public int getLineForHorizontal(int horizontal) {
		int high = getLineCount(), low = -1, guess;

        while (high - low > 1) {
            guess = (high + low) / 2;

            if (getLineTop(guess) > horizontal)
                high = guess;
            else
                low = guess;
        }

        if (low < 0)
            return 0;
        else
            return low;
	}
	
	private int getOffsetAtStartOf(int offset) {
        if (offset == 0)
            return 0;

        CharSequence text = getText();
        char c = text.charAt(offset);

        if (c >= '\uDC00' && c <= '\uDFFF') {
            char c1 = text.charAt(offset - 1);

            if (c1 >= '\uD800' && c1 <= '\uDBFF')
                offset -= 1;
        }

        if (isSpanned()) {
            ReplacementSpan[] spans = ((Spanned) text).getSpans(offset, offset,
                                                       ReplacementSpan.class);

            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);

                if (start < offset && end > offset)
                    offset = start;
            }
        }

        return offset;
    }

	private int MAX_EMOJI  = -1;

	private static final ParagraphStyle[] NO_PARA_SPANS = new ParagraphStyle[0];
	public MongolianDynamicLayout(CharSequence base, TextPaint paint,
			int width, Alignment align, float spacingmult, float spacingadd,
			boolean includepad) {
		super(base, paint, width, align, spacingmult, spacingadd, includepad);
		// TODO Auto-generated constructor stub
	}

	public MongolianDynamicLayout(CharSequence base, CharSequence display,
			TextPaint paint, int width, Alignment align, float spacingmult,
			float spacingadd, boolean includepad, TruncateAt ellipsize,
			int ellipsizedWidth) {
		super(base, display, paint, width, align, spacingmult, spacingadd,
				includepad, ellipsize, ellipsizedWidth);
	}

	public MongolianDynamicLayout(CharSequence base, CharSequence display,
			TextPaint paint, int width, Alignment align, float spacingmult,
			float spacingadd, boolean includepad) {
		super(base, display, paint, width, align, spacingmult, spacingadd,
				includepad);
		// TODO Auto-generated constructor stub
	}

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

		Log.i("DL","highlight is null ? " + (highlight==null ));
		int top = 0;
		int bottom = getLineTop(getLineCount());
		
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
		// the layout or line. XXX: Should they?
		if (spannedText) {
			textLength = buf.length();
			for (int i = first; i <= last; i++) {
				int start = previousLineEnd;
				int end = getLineStart(i + 1);
				previousLineEnd = end;

				int ltop = previousLineBottom;
				int lbottom = -getLineTop(i + 1);
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

					back.drawBackground(c, paint, 0, width, ltop, lbaseline,
							lbottom, buf, start, end, i);
				}
			}
			// reset to their original values
			spanend = 0;
			previousLineBottom = -getLineTop(first);
			previousLineEnd = getLineStart(first);
			spans = NO_PARA_SPANS;
		}

		c.save();
		c.scale(1.0f, -1.0f, .0f, .0f);
		c.rotate(-90.0f, .0f, .0f);
		
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
		
		c.restore();
		

		Alignment align = getAlignment();
		
		PorterDuffXfermode clearPorterDuffXfermode = new PorterDuffXfermode(Mode.CLEAR);
		Paint clearPaint = new Paint();
		clearPaint.setXfermode(clearPorterDuffXfermode);

		// Next draw the lines, one at a time.
		// the baseline is the top of the following line minus the current
		// line's descent.

		c.rotate(90.0f);

		for (int i = first; i <= last; i++) {
			int start = previousLineEnd;

			previousLineEnd = getLineStart(i + 1);
			int end = getLineVisibleEnd(i, start, previousLineEnd);

			int ltop = previousLineBottom;
			int lbottom = -getLineTop(i + 1);
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
					for (int n = spans.length - 1; n >= 0; n--) {
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

			// Draw all leading margin spans. Adjust left or right according
			// to the paragraph direction of the line.
			if (spannedText) {
				final int length = spans.length;
				for (int n = 0; n < length; n++) {
					if (spans[n] instanceof LeadingMarginSpan) {
						LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];

						if (dir == DIR_RIGHT_TO_LEFT) {
							margin.drawLeadingMargin(c, paint, right, dir,
									ltop, lbaseline, lbottom, buf, start, end,
									isFirstParaLine, this);

							right -= margin.getLeadingMargin(isFirstParaLine);
						} else {
							margin.drawLeadingMargin(c, paint, left, dir, ltop,
									lbaseline, lbottom, buf, start, end,
									isFirstParaLine, this);

							boolean useMargin = isFirstParaLine;
							if (margin instanceof LeadingMarginSpan.LeadingMarginSpan2) {
								int count = ((LeadingMarginSpan.LeadingMarginSpan2) margin)
										.getLeadingMarginLineCount();
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
				int max;
				if (VERSION.SDK_INT >= 15) {
					max = (int) getLineMaxHighAPI(i);
				} else {
					max = (int) getLineMax(i, spans, false);
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
			if (directions == DIRS_ALL_LEFT_TO_RIGHT && !spannedText && !hasTab) {
				c.drawText(buf, start, end, x, lbaseline, paint);
				//Log.d("MongolianTextView.DL", "drawing here ");

				/*
				if(isContainsCJK(buf,start,end)){
					//Log.d("MongolianTextView.DL", "has CJKs");
            		String reg = 
            				"\\p{InCJK_Unified_Ideographs}|" +
            				"\\p{InHiragana}|\\p{InKatakana}|" +
            				"\\p{InHangul_Compatibility_Jamo}|" +
            				"\\p{InHangul_Jamo}|" +
            				"\\p{InHangul_Syllables}";
            		Matcher matcher = Pattern.compile(reg).matcher(buf).region(start, end);
            		
            		while (matcher.find()) {
						int idx= matcher.start();
						RectF cjkRect = new RectF();
						
						float xLeft = paint.measureText(buf,start,idx);
						float xWidth = paint.measureText(buf,idx,idx+1);
						cjkRect.left = xLeft;
						cjkRect.top = lbottom;
						cjkRect.right = cjkRect.left + xWidth;
						cjkRect.bottom = cjkRect.top + paint.getFontMetrics().bottom-paint.getFontMetrics().top;
						c.drawRect(cjkRect, clearPaint);
						
						c.save();
						c.rotate(-90, (int)cjkRect.centerX(), (int)cjkRect.centerY());
						c.translate(0, paint.getFontMetrics().top - paint.getFontMetrics().ascent);
						c.drawText((String) buf.subSequence(idx, idx+1), xLeft,lbaseline , paint);
						
						c.restore();
					}
            	}*/
               
			} else {
				TextPaint mWorkPaint = getWorkingPaint();
				// if(VERSION.SDK_INT>=15){
				// Object tl = TextLineEmulator.obtain();
				//
				// TextLineEmulator.set(tl, mPaint, buf, start, end, dir,
				// directions, hasTab, null);
				// TextLineEmulator.draw(tl, c, x, ltop, lbaseline, lbottom);
				// }
				// else{
				//
				//Log.d("MongolianTextView.DL", "drawing here 1 " + start +"->" + end  );
				drawText(c, buf, start, end, dir, directions, x, ltop,
						lbaseline, lbottom, paint, mWorkPaint, hasTab, spans);
				// }
				
				/*
				if(isContainsCJK(buf,start,end)){
            		String reg = 
            				"\\p{InCJK_Unified_Ideographs}|" +
            				"\\p{InHiragana}|\\p{InKatakana}|" +
            				"\\p{InHangul_Compatibility_Jamo}|" +
            				"\\p{InHangul_Jamo}|" +
            				"\\p{InHangul_Syllables}";
            		//Matcher matcher = Pattern.compile(reg).matcher(buf).region(start, end);
            		

            		while (matcher.find()) {
						int idx= matcher.start();
						RectF cjkRect = new RectF();
						
						float xLeft = paint.measureText(buf,start,idx);
						float xWidth = paint.measureText(buf,idx,idx+1);
						/*cjkRect.left = xLeft;
						cjkRect.top = lbottom;
						cjkRect.right = cjkRect.left + xWidth;
						cjkRect.bottom = cjkRect.top + paint.getFontMetrics().bottom-paint.getFontMetrics().top;
						c.drawRect(cjkRect, clearPaint);
						
						c.save();
						c.rotate(-90, (int)cjkRect.centerX(), (int)cjkRect.centerY());
						c.translate(0, paint.getFontMetrics().top - paint.getFontMetrics().ascent);
						c.drawText(buf.subSequence(idx, idx+1).toString(), xLeft,lbaseline , paint);
						
						c.restore();
					}
            	}*/

			}
		}

		c.rotate(-90.0f);
	}

	private TextPaint getWorkingPaint() {
		return mWorkPaint;
	}
	public static boolean isContainsCJK(CharSequence buff,int start,int end) {
		if (buff == null) {
			return false;
		}
		String reg = 
				"\\p{InCJK_Unified_Ideographs}|" +
				"\\p{InHiragana}|\\p{InKatakana}|" +
				"\\p{InHangul_Compatibility_Jamo}|" +
				"\\p{InHangul_Jamo}|" +
				"\\p{InHangul_Syllables}";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(buff).region(start, end).find();
	}
	
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
                //Log.e("#LT DrawLine", "Drawing Line  : " + TextUtils.substring(text, start, end));
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
    	   mDirections = GetDirections_mDirections(directions);
       else {
    	   short [] temp = GetDirections_mDirectionsShort(directions);
    	   mDirections = new int[temp.length];
    	   for (int i =0;i<temp.length;i++) {
    		   mDirections [i] = 0 + temp[i];
    	   }
       }
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

                            canvas.drawBitmap(bm, null, mEmojiRect, paint);
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

	/*
	 * Methods From Layout ***********
	 */
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


	static Method method_getLineMax;
	@SuppressWarnings("rawtypes")
	private float getLineMax(int line, Object[] tabs, boolean full) {

		if(method_getLineMax==null){
			Class[] parameterTypes = new Class[] { int.class, Object[].class,
					boolean.class };
			try {
				method_getLineMax = Layout.class.getDeclaredMethod("getLineMax",
                        parameterTypes);
				method_getLineMax.setAccessible(true);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		try {
			return (Float) method_getLineMax.invoke(this, line, tabs, full);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}


	static Method method_getLineMaxHighAPI;
	private float getLineMaxHighAPI(int line) {


		if(method_getLineMaxHighAPI == null){
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[] { int.class };
			try {
				method_getLineMaxHighAPI = Layout.class.getDeclaredMethod("getLineMax",
                        parameterTypes);
				method_getLineMaxHighAPI.setAccessible(true);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		try {
			return (Float) method_getLineMaxHighAPI.invoke(this, line);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}


	static Field field_DIRS_ALL_LEFT_TO_RIGHT;
	private Directions getDIRS_ALL_LEFT_TO_RIGHT() {
		// TODO Auto-generated method stub
		if(field_DIRS_ALL_LEFT_TO_RIGHT==null){
			try {
				field_DIRS_ALL_LEFT_TO_RIGHT= Layout.class.getDeclaredField("DIRS_ALL_LEFT_TO_RIGHT");
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			field_DIRS_ALL_LEFT_TO_RIGHT.setAccessible(true);
		}
		try {
			return (Directions) field_DIRS_ALL_LEFT_TO_RIGHT.get(null);
		} catch (SecurityException e) {
			
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	static Field field_mDirectionsShort;
	/*��sdk<15Directions���������ΪShort
	 * */
	private short[] GetDirections_mDirectionsShort(Directions directions) {
		// TODO Auto-generated method stub

		if(field_mDirectionsShort == null ){
			try {
				field_mDirectionsShort= Directions.class.getDeclaredField("mDirections");
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			field_mDirectionsShort.setAccessible(true);
		}
		try {
			return (short[])field_mDirectionsShort.get(directions);
		} catch (SecurityException e) {
			e.printStackTrace();
		}  catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return null;
	}


	static Field field_mDirections;
	public int[] GetDirections_mDirections(Directions directions)
	{
		if(field_mDirections==null){
			try {
				field_mDirections= Directions.class.getDeclaredField("mDirections");
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			field_mDirections.setAccessible(true);
		}
		try {
			return (int[])field_mDirections.get(directions);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}


	static Method method_Layout_nextTab;
	@SuppressWarnings("rawtypes")
	private float Layout_nextTab(CharSequence text, int start, int end, float f,
			Object[] parspans) {

		if(method_Layout_nextTab==null) {
			Class[] parameterTypes = new Class[]{
					CharSequence.class, int.class, int.class,
					float.class, Object[].class
			};
			try {
				method_Layout_nextTab = Layout.class.getDeclaredMethod("nextTab",parameterTypes);
				method_Layout_nextTab.setAccessible(true);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

		}
			try {

				return (Float) method_Layout_nextTab.invoke(null,
						text, start, end, f,
						parspans);
			}  catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		return 0;
	}
	private void TextUtils_recycle(char[] buf) {

	}

}
