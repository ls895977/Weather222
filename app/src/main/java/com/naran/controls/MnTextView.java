package com.naran.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by MENK021 on 2016/12/26.
 */

public class MnTextView extends com.menksoft.android.widget.MongolianTextView{

    public MnTextView(Context context) {
        super(context);
        init(context);
    }

    public MnTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MnTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/mkhw.ttf");
        this.setTypeface(typeface);
    }
}
