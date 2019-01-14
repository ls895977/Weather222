package com.naran.controls;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naran.model.WranAndServiceModel;
import com.naran.weather.R;
import com.naran.weather.WebViewActivity;

import java.util.List;

/**
 * Created by darhandarhad on 2017/7/9.
 */

public class CnArticalView5 extends LinearLayout{
    private Context context;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;

    private TextView time1;
    private TextView time2;
    private TextView time3;
    private TextView time4;
    private TextView time5;
    private TextView time6;
    private TextView time7;
    private TextView time8;

    public CnArticalView5(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CnArticalView5(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CnArticalView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }
    public void init(){

        LayoutInflater.from(context).inflate(R.layout.view_title_time, this);
        tv1 = (TextView)this.findViewById(R.id.tv1);
        tv2 = (TextView)this.findViewById(R.id.tv2);
        tv3 = (TextView)this.findViewById(R.id.tv3);
        tv4 = (TextView)this.findViewById(R.id.tv4);
        tv5 = (TextView)this.findViewById(R.id.tv5);
        tv6 = (TextView)this.findViewById(R.id.tv6);
        tv7 = (TextView)this.findViewById(R.id.tv7);
        tv8 = (TextView)this.findViewById(R.id.tv8);

        time1 = (TextView)this.findViewById(R.id.time1);
        time2 = (TextView)this.findViewById(R.id.time2);
        time3 = (TextView)this.findViewById(R.id.time3);
        time4 = (TextView)this.findViewById(R.id.time4);
        time5 = (TextView)this.findViewById(R.id.time5);
        time6 = (TextView)this.findViewById(R.id.time6);
        time7 = (TextView)this.findViewById(R.id.time7);
        time8 = (TextView)this.findViewById(R.id.time8);

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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(0).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(0).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(1).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(1).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(2).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(2).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(3).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(3).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(4).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time5.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(4).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(5).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time6.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(5).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(6).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time7.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(6).getWarningAndServiceID());
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
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(7).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
                this.time8.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("imgUrl","http://121.41.123.152:8000/ViewInfo.aspx?ID="+wranAndServiceModels.get(7).getWarningAndServiceID());
                        context.startActivity(intent);
                    }
                });
            }else{
                break;
            }
        }
    }
}
