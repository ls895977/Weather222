package com.naran.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.naran.weather.R;
import com.naran.weather.WebViewActivity;

import java.util.List;

/**
 * Created by MENK021 on 2017/1/1.
 */

public class MnArticalView extends LinearLayout {

    private int mMeasuredHeight = -1;
    private int mMeasuredWidth = -1;
    private static final String ELLIIPSIS_STR = "";
    private int mBreakTextCount = 0;
    private Context context;
    private MnTextView titlesView;
    private MnTextView title;
    private static int itemLineMargin = 10;
    private List<ArticalModel> articalModels;

    public MnArticalView(Context context) {
        super(context);
        init(context);
    }

    public MnArticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MnArticalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_mnartical,this);
        titlesView =(MnTextView)this.findViewById(R.id.articalTitles);
        titlesView.setLineSpacing(itemLineMargin, 1);
    }
    public void setContent(String title,List<ArticalModel> articalModels){
        this.articalModels = articalModels;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = titlesView.getMeasuredHeight();
        int measuredWidth = titlesView.getMeasuredWidth();
        if(mMeasuredHeight!=measuredHeight||mMeasuredWidth!=measuredWidth){
            mMeasuredHeight = measuredHeight;
            mMeasuredWidth = measuredWidth;

            int drawHeight = measuredHeight
                    - titlesView.getCompoundPaddingBottom()
                    - titlesView.getCompoundPaddingTop();
            int drawWidth = measuredWidth
                    - titlesView.getCompoundPaddingLeft()
                    - titlesView.getCompoundPaddingRight();

            Log.d("NewArticle",measuredWidth + ","+measuredHeight);
            Log.d("NewArticle", "draw with "+drawWidth+","+drawHeight);

            TextPaint paint = titlesView.getPaint();

            Paint.FontMetrics fm = paint.getFontMetrics();

            float lineWidth =   (float) (Math.ceil(fm.descent - fm.ascent) )+itemLineMargin;
            int maxLine = (int) (drawWidth/lineWidth);

            titlesView.setText("");
            int i=0;
            if(null!=articalModels)
            for (ArticalModel itemArticleTitle : articalModels) {
                if(maxLine==i)
                    break;
                String trimedTitle = itemArticleTitle.getTitle().trim();
                SpannableString spStr = getEllipsisedString(trimedTitle,drawHeight,paint,itemArticleTitle.getID());
                titlesView.append(spStr);
                i++;
            }
            titlesView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    public int Dp2Px( float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(float px) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public  int GetMaxItemSize(Activity activity){
        int disHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int disWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

        float maxSize = disHeight;
        if(maxSize<disWidth)
            maxSize = disWidth;

        float titleSize = Px2Dp(40.0f);
        float fontSize = Px2Dp(32.0f);
        float contentLayoutSize =  maxSize - titleSize;
        return (int) (contentLayoutSize/fontSize);
    }
    private class NoLineClickSpan extends ClickableSpan {
        private int mId;
        public NoLineClickSpan(int id) {
            super();
            this.mId = id;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(context, WebViewActivity.class);
            Log.e("aaaa","id = "+mId);
            intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+mId);
            context.startActivity(intent);
            //Toast.makeText(mActivity, titleListTextView.getMeasuredWidth()+"", 1000).show();
        }
    }
    private SpannableString getEllipsisedString(String str, float height, Paint paint, int id)
    {
        str = str.trim();
        float subHeight = paint.measureText(str);
        if(subHeight<=height)
        {
            SpannableString spStr =  new SpannableString(str+"\n");
            ClickableSpan clickSpan = new NoLineClickSpan(id);
            spStr.setSpan(clickSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spStr;
        }
        float ellipseHeight = paint.measureText(ELLIIPSIS_STR);
        int index = paint.breakText(str, true, height-ellipseHeight, null);
        if(str.length()>index+1){
            if(str.charAt(index)!=' ')
            {
                while(str.charAt(index)!=' '&&index>0)
                    index --;
            }
        }
        SpannableString spStr =  new SpannableString(str.substring(0, index)+ELLIIPSIS_STR+"\n");
        ClickableSpan clickSpan = new NoLineClickSpan(id);
        spStr.setSpan(clickSpan, 0, index+ELLIIPSIS_STR.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spStr;
    }
}
