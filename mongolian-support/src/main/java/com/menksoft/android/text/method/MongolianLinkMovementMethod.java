package com.menksoft.android.text.method;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by kermor on 2016-7-6.
 */
public class MongolianLinkMovementMethod extends LinkMovementMethod {
    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return false;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        //MotionEvent.obtain(event);
        event.setLocation(event.getY(),event.getX());
        Boolean handled =  super.onTouchEvent(widget, buffer, event);
        event.setLocation(event.getY(),event.getX());
        return handled;
    }

    static MongolianLinkMovementMethod sInstance;
    public static MongolianLinkMovementMethod getInstance(){
        if (sInstance == null) {
            sInstance = new MongolianLinkMovementMethod();
        }

        return sInstance;
    }
}
