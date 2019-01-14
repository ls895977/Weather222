package com.naran.welcome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.naran.navigation.SelectLanguageActivity;
import com.naran.weather.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private List<View> views;
    private ImageView img3;
    private SharedPreferences shared;
    private int versionCode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
      //  getPromiss();
        shared = getSharedPreferences("firstTime", Context.MODE_PRIVATE);
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(versionCode!=-1) {
            String isFirstTime = shared.getString("firstTime", "");
            if (isFirstTime.equals(""+versionCode)) {
                startActivity(new Intent().setClass(WelcomeActivity.this, SelectLanguageActivity.class));
                finish();
            }
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("firstTime", ""+versionCode);
            editor.commit();
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.cursorLayout);
        views = new ArrayList<>();
        View view1 = getLayoutInflater().inflate(R.layout.view_welcome, null);
        ImageView img1 = (ImageView) view1.findViewById(R.id.cursorImg);
        img1.setImageDrawable(getResources().getDrawable(R.drawable.notic));
        View view2 = getLayoutInflater().inflate(R.layout.view_welcome, null);
        ImageView img2 = (ImageView) view2.findViewById(R.id.cursorImg);
        img2.setImageDrawable(getResources().getDrawable(R.drawable.noticone));
        View view3 = getLayoutInflater().inflate(R.layout.view_welcome, null);
        img3 = (ImageView) view3.findViewById(R.id.cursorImg);
        img3.setImageDrawable(getResources().getDrawable(R.drawable.notictow));
        
        views.add(view1);
        views.add(view2);
        views.add(view3);

        for (int i = 0; i < views.size(); i++) {

            View view = new View(this);
            if (i == 0) {
                view.setBackgroundColor(Color.BLUE);
            } else {

                view.setBackgroundColor(Color.YELLOW);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.setMargins(8, 0, 8, 0);
            view.setLayoutParams(layoutParams);
            linearLayout.addView(view);
        }
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < views.size(); i++) {
                    if (i == position) {
                        View view = linearLayout.getChildAt(i);
                        view.setBackgroundColor(Color.BLUE);
                    } else {
                        View view = linearLayout.getChildAt(i);
                        view.setBackgroundColor(Color.YELLOW);
                    }
                }
                if (position == views.size() - 1) {
                    img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent().setClass(WelcomeActivity.this, SelectLanguageActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }
    }
    private void getPromiss(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (WelcomeActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(WelcomeActivity.this,permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
}
