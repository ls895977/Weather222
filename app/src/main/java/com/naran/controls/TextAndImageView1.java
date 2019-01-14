package com.naran.controls;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naran.weather.R;
import com.naran.weather.ShowImageActivity;

/**
 * Created by MENK021 on 2017/1/2.
 */

public class TextAndImageView1 extends LinearLayout{
    TextView tvFirs,tvSecond,tvThird;
    ImageView imgFirst,imgSecond,imgThird;
    public String imgUrl1,imgUrl2,imgUrl3;

    public TextView getTvFirs() {
        return tvFirs;
    }

    public void setTvFirs(TextView tvFirs) {
        this.tvFirs = tvFirs;
    }

    public TextView getTvSecond() {
        return tvSecond;
    }

    public void setTvSecond(TextView tvSecond) {
        this.tvSecond = tvSecond;
    }

    public TextView getTvThird() {
        return tvThird;
    }

    public void setTvThird(TextView tvThird) {
        this.tvThird = tvThird;
    }

    public ImageView getImgFirst() {
        return imgFirst;
    }

    public void setImgFirst(ImageView imgFirst) {
        this.imgFirst = imgFirst;
    }

    public ImageView getImgSecond() {
        return imgSecond;
    }

    public void setImgSecond(ImageView imgSecond) {
        this.imgSecond = imgSecond;
    }

    public ImageView getImgThird() {
        return imgThird;
    }

    public void setImgThird(ImageView imgThird) {
        this.imgThird = imgThird;
    }

    public TextAndImageView1(Context context) {
        super(context);
        init(context);
    }

    public TextAndImageView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextAndImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.textandimage1, this);
        tvFirs = (TextView) this.findViewById(R.id.firstText);
        imgFirst = (ImageView) this.findViewById(R.id.firstImg);
        tvSecond = (TextView) this.findViewById(R.id.secondText);
        imgSecond = (ImageView) this.findViewById(R.id.secondImg);
        tvThird = (TextView) this.findViewById(R.id.thirdText);
        imgThird = (ImageView) this.findViewById(R.id.thirdImg);
        imgFirst.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("imgUrl",imgUrl1);
                intent.setClass(context,ShowImageActivity.class);
                context.startActivity(intent);
            }
        });
        imgSecond.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("imgUrl",imgUrl2);
                intent.setClass(context,ShowImageActivity.class);
                context.startActivity(intent);
            }
        });
        imgThird.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("imgUrl",imgUrl3);
                intent.setClass(context,ShowImageActivity.class);
                context.startActivity(intent);
            }
        });

        tvFirs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFirst.setVisibility(View.VISIBLE);
                imgSecond.setVisibility(View.GONE);
                imgThird.setVisibility(View.GONE);
            }
        });
        tvSecond.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFirst.setVisibility(View.GONE);
                imgSecond.setVisibility(View.VISIBLE);
                imgThird.setVisibility(View.GONE);
            }
        });
        tvThird.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFirst.setVisibility(View.GONE);
                imgSecond.setVisibility(View.GONE);
                imgThird.setVisibility(View.VISIBLE);
            }
        });
    }
}
