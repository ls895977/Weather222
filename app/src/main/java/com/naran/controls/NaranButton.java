package com.naran.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MENK021 on 2017/1/1.
 */

public class NaranButton extends TextView{
    private Typeface typeface;
    public NaranButton(Context context) {
        super(context);
        init(context);
    }

    public NaranButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NaranButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/fontUI.ttf");
        this.setTypeface(typeface);
    }
}
