package com.naran.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * Created by darhandarhad on 2017/11/20.
 */

@SuppressLint("AppCompatCustomView")
public class MnCheckBox extends LinearLayout {


    public MnCheckBox(Context context) {
        super(context);
    }

    public MnCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        CheckBox checkBox = new CheckBox(context);
    }

    public MnCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
