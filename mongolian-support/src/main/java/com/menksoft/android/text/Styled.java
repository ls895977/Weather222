package com.menksoft.android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;

public class Styled {
	static float drawText(Canvas canvas, CharSequence text, int start, int end,
			int dir, boolean runIsRtl, float x, int top, int y, int bottom,
			TextPaint paint, TextPaint workPaint, boolean needWidth) {
		// XXX this logic is (dir == DIR_LEFT_TO_RIGHT) == runIsRtl
		if ((dir == Layout.DIR_RIGHT_TO_LEFT && !runIsRtl)
				|| (runIsRtl && dir == Layout.DIR_LEFT_TO_RIGHT)) {
			// TODO: this needs the real direction
			float ch = drawDirectionalRun(null, text, start, end,
					Layout.DIR_LEFT_TO_RIGHT, false, 0, 0, 0, 0, null, paint,
					workPaint, true);

			ch *= dir; // DIR_RIGHT_TO_LEFT == -1
			drawDirectionalRun(canvas, text, start, end, -dir, runIsRtl,
					x + ch, top, y, bottom, null, paint, workPaint, true);

			return ch;
		}

		return drawDirectionalRun(canvas, text, start, end, dir, runIsRtl, x,
				top, y, bottom, null, paint, workPaint, needWidth);
	}

	private static float drawDirectionalRun(Canvas canvas, CharSequence text,
			int start, int end, int dir, boolean runIsRtl, float x, int top,
			int y, int bottom, Paint.FontMetricsInt fmi, TextPaint paint,
			TextPaint workPaint, boolean needWidth) {

		// XXX: It looks like all calls to this API match dir and runIsRtl, so
		// having both parameters is redundant and confusing.

		// fast path for unstyled text
		if (!(text instanceof Spanned)) {
			float ret = 0;

			if (runIsRtl) {
				CharSequence tmp = TextUtils.getReverse(text, start, end);
				// XXX: this assumes getReverse doesn't tweak the length of
				// the text
				int tmpend = end - start;

				if (canvas != null || needWidth)
					ret = paint.measureText(tmp, 0, tmpend);

				if (canvas != null)
					canvas.drawText(tmp, 0, tmpend, x - ret, y, paint);
			} else {
				if (needWidth)
					ret = paint.measureText(text, start, end);

				if (canvas != null)
					canvas.drawText(text, start, end, x, y, paint);
			}

			if (fmi != null) {
				paint.getFontMetricsInt(fmi);
			}

			return ret * dir; // Layout.DIR_RIGHT_TO_LEFT == -1
		}

		float ox = x;
		int minAscent = 0, maxDescent = 0, minTop = 0, maxBottom = 0;

		Spanned sp = (Spanned) text;
		Class<?> division;

		if (canvas == null)
			division = MetricAffectingSpan.class;
		else
			division = CharacterStyle.class;

		int next;
		for (int i = start; i < end; i = next) {
			next = sp.nextSpanTransition(i, end, division);

			// XXX: if dir and runIsRtl were not the same, this would draw
			// spans in the wrong order, but no one appears to call it this
			// way.
			x += drawUniformRun(canvas, sp, i, next, dir, runIsRtl, x, top, y,
					bottom, fmi, paint, workPaint, needWidth || next != end);

			if (fmi != null) {
				if (fmi.ascent < minAscent)
					minAscent = fmi.ascent;
				if (fmi.descent > maxDescent)
					maxDescent = fmi.descent;

				if (fmi.top < minTop)
					minTop = fmi.top;
				if (fmi.bottom > maxBottom)
					maxBottom = fmi.bottom;
			}
		}

		if (fmi != null) {
			if (start == end) {
				paint.getFontMetricsInt(fmi);
			} else {
				fmi.ascent = minAscent;
				fmi.descent = maxDescent;
				fmi.top = minTop;
				fmi.bottom = maxBottom;
			}
		}

		return x - ox;
	}

	private static float drawUniformRun(Canvas canvas, Spanned text, int start,
			int end, int dir, boolean runIsRtl, float x, int top, int y,
			int bottom, Paint.FontMetricsInt fmi, TextPaint paint,
			TextPaint workPaint, boolean needWidth) {

		boolean haveWidth = false;
		float ret = 0;
		CharacterStyle[] spans = text
				.getSpans(start, end, CharacterStyle.class);

		ReplacementSpan replacement = null;

		// XXX: This shouldn't be modifying paint, only workPaint.
		// However, the members belonging to TextPaint should have default
		// values anyway. Better to ensure this in the Layout constructor.
		paint.bgColor = 0;
		paint.baselineShift = 0;
		workPaint.set(paint);

		if (spans.length > 0) {
			for (int i = 0; i < spans.length; i++) {
				CharacterStyle span = spans[i];

				if (span instanceof ReplacementSpan) {
					replacement = (ReplacementSpan) span;
				} else {
					span.updateDrawState(workPaint);
				}
			}
		}

		if (replacement == null) {
			CharSequence tmp;
			int tmpstart, tmpend;

			if (runIsRtl) {
				tmp = TextUtils.getReverse(text, start, end);
				tmpstart = 0;
				// XXX: assumes getReverse doesn't change the length of the text
				tmpend = end - start;
			} else {
				tmp = text;
				tmpstart = start;
				tmpend = end;
			}

			if (fmi != null) {
				workPaint.getFontMetricsInt(fmi);
			}

			if (canvas != null) {
				if (workPaint.bgColor != 0) {
					int c = workPaint.getColor();
					Paint.Style s = workPaint.getStyle();
					workPaint.setColor(workPaint.bgColor);
					workPaint.setStyle(Paint.Style.FILL);

					if (!haveWidth) {
						ret = workPaint.measureText(tmp, tmpstart, tmpend);
						haveWidth = true;
					}

					if (dir == Layout.DIR_RIGHT_TO_LEFT)
						canvas.drawRect(x - ret, top, x, bottom, workPaint);
					else
						canvas.drawRect(x, top, x + ret, bottom, workPaint);

					workPaint.setStyle(s);
					workPaint.setColor(c);
				}

				if (dir == Layout.DIR_RIGHT_TO_LEFT) {
					if (!haveWidth) {
						ret = workPaint.measureText(tmp, tmpstart, tmpend);
						haveWidth = true;
					}

					canvas.drawText(tmp, tmpstart, tmpend, x - ret, y
							+ workPaint.baselineShift, workPaint);
				} else {
					if (needWidth) {
						if (!haveWidth) {
							ret = workPaint.measureText(tmp, tmpstart, tmpend);
							haveWidth = true;
						}
					}

					canvas.drawText(tmp, tmpstart, tmpend, x, y
							+ workPaint.baselineShift, workPaint);
				}
			} else {
				if (needWidth && !haveWidth) {
					ret = workPaint.measureText(tmp, tmpstart, tmpend);
					haveWidth = true;
				}
			}
		} else {
			ret = replacement.getSize(workPaint, text, start, end, fmi);

			/******************************/
			if (replacement instanceof ImageSpan) {
				canvas.save();
				canvas.rotate(-90.f, x, bottom);
				Drawable drawable = ((ImageSpan) replacement).getDrawable();
				float w = (float) drawable.getIntrinsicWidth();
				float h = (float) drawable.getIntrinsicHeight();
				float z = w / h;
				canvas.scale(z, 1.0f / z, x, bottom);
				canvas.translate(-h, w);
			}
			if (canvas != null) {
				if (dir == Layout.DIR_RIGHT_TO_LEFT)
					replacement.draw(canvas, text, start, end, x - ret, top, y,
							bottom, workPaint);
				else
					replacement.draw(canvas, text, start, end, x, top, y,
							bottom, workPaint);
			}
			if (replacement instanceof ImageSpan) {
				canvas.restore();
			}
			/************************************/
		}

		if (dir == Layout.DIR_RIGHT_TO_LEFT)
			return -ret;
		else
			return ret;
	}

	public static float measureText(TextPaint paint, TextPaint workPaint,
			CharSequence text, int start, int end, Paint.FontMetricsInt fmi) {
		// TODO Auto-generated method stub
		return drawDirectionalRun(null, text, start, end,
				Layout.DIR_LEFT_TO_RIGHT, false, 0, 0, 0, 0, fmi, paint,
				workPaint, true);
	}
}
