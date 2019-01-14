package com.naran.controls;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.naran.model.WranAndServiceModel;
import com.naran.weather.R;
import com.naran.weather.WebViewActivity;

import java.util.List;

/**
 * Created by darhandarhad on 2017/7/9.
 */

public class MnArticalView8 extends LinearLayout{
    private Context context;
    private MnTextView tv1;
    private MnTextView tv2;
    private MnTextView tv3;
    private MnTextView tv4;
    private MnTextView tv5;
    private MnTextView tv6;
    private MnTextView tv7;
    private MnTextView tv8;

    private MnTextView time1;
    private MnTextView time2;
    private MnTextView time3;
    private MnTextView time4;
    private MnTextView time5;
    private MnTextView time6;
    private MnTextView time7;
    private MnTextView time8;

    public MnArticalView8(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MnArticalView8(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MnArticalView8(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    public void init(){

        LayoutInflater.from(context).inflate(R.layout.view_title_time_mn, this);
        tv1 = (MnTextView)this.findViewById(R.id.tv1);
        tv2 = (MnTextView)this.findViewById(R.id.tv2);
        tv3 = (MnTextView)this.findViewById(R.id.tv3);
        tv4 = (MnTextView)this.findViewById(R.id.tv4);
        tv5 = (MnTextView)this.findViewById(R.id.tv5);
        tv6 = (MnTextView)this.findViewById(R.id.tv6);
        tv7 = (MnTextView)this.findViewById(R.id.tv7);
        tv8 = (MnTextView)this.findViewById(R.id.tv8);

        time1 = (MnTextView)this.findViewById(R.id.time1);
        time2 = (MnTextView)this.findViewById(R.id.time2);
        time3 = (MnTextView)this.findViewById(R.id.time3);
        time4 = (MnTextView)this.findViewById(R.id.time4);
        time5 = (MnTextView)this.findViewById(R.id.time5);
        time6 = (MnTextView)this.findViewById(R.id.time6);
        time7 = (MnTextView)this.findViewById(R.id.time7);
        time8 = (MnTextView)this.findViewById(R.id.time8);

    }
    public void setData(final List<WranAndServiceModel> wranAndServiceModels){

        for(int i=0;i<wranAndServiceModels.size();i++){
            if(i==0) {
                this.tv1.setVisibility(View.VISIBLE);
                this.time1.setVisibility(View.VISIBLE);
                this.tv1.setText(wranAndServiceModels.get(i).getTitle());
                this.time1.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(0).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(0).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else if(i==1) {
                this.tv2.setVisibility(View.VISIBLE);
                this.time2.setVisibility(View.VISIBLE);
                this.tv2.setText(wranAndServiceModels.get(i).getTitle());
                this.time2.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(1).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(1).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }
            else if(i==2) {
                this.tv3.setVisibility(View.VISIBLE);
                this.time3.setVisibility(View.VISIBLE);
                this.tv3.setText(wranAndServiceModels.get(i).getTitle());
                this.time3.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(2).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(2).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }
            else if(i==3) {
                this.tv4.setVisibility(View.VISIBLE);
                this.time4.setVisibility(View.VISIBLE);
                this.tv4.setText(wranAndServiceModels.get(i).getTitle());
                this.time4.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(3).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(3).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }
            else if(i==4) {
                this.tv5.setVisibility(View.VISIBLE);
                this.time5.setVisibility(View.VISIBLE);
                this.tv5.setText(wranAndServiceModels.get(i).getTitle());
                this.time5.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv5.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(4).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time5.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(4).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else if(i==5) {
                this.tv6.setVisibility(View.VISIBLE);
                this.time6.setVisibility(View.VISIBLE);
                this.tv6.setText(wranAndServiceModels.get(i).getTitle());
                this.time6.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv6.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(5).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time6.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(5).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else if(i==6) {
                this.tv7.setVisibility(View.VISIBLE);
                this.time7.setVisibility(View.VISIBLE);
                this.tv7.setText(wranAndServiceModels.get(i).getTitle());
                this.time7.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv7.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(6).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time7.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(6).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else if(i==7) {
                this.tv8.setVisibility(View.VISIBLE);
                this.time8.setVisibility(View.VISIBLE);
                this.tv8.setText(wranAndServiceModels.get(i).getTitle());
                this.time8.setText(wranAndServiceModels.get(i).getAddTime());
                this.tv8.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(7).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time8.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfos.aspx?ID="+wranAndServiceModels.get(7).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else{
                break;
            }
        }
    }
}
