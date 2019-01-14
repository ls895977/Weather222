package com.naran.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.naran.controls.TouchImageView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowImageActivity extends Activity {

    String imgUrl = "";
    TouchImageView touchImageView;
    private Handler handler;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        handler = new Handler();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        touchImageView = (TouchImageView)findViewById(R.id.touchImage);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        imgUrl = getIntent().getStringExtra("imgUrl");
        if(null == imgUrl|| imgUrl.equals("")){

            Toast.makeText(this, "图片路径错误！", Toast.LENGTH_SHORT).show();
            finish();
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
        Request request = new Request.Builder()
                .url(imgUrl)
                .build();
        //3.创建一个Call对象，参数是request对象，发送请求
        Call call = okHttpClient.newCall(request);
        //4.异步请求，请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到从网上获取资源，转换成我们想要的类型
                byte[] Picture_bt = response.body().bytes();
                //通过handler更新UI

                final Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                //通过imageview，设置图片
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        touchImageView.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
