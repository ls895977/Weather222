package com.naran.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naran.weather.Main1Activity;
import com.naran.weather.MainActivity;
import com.naran.weather.R;

import java.util.Locale;


public class SelectLanguageActivity extends Activity {

    private Button btnCn, btnMn;
    private LinearLayout languageLyout;
    private TextView tvVersion;
    private int versionCode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        initViews();
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(-1!=versionCode){
            tvVersion.setText("version  "+versionCode+"");
        }
        getPremiss();
    }

    private void initViews() {
        tvVersion = (TextView)findViewById(R.id.versionCode);
        languageLyout = (LinearLayout) findViewById(R.id.languageLyout);
        btnCn = (Button) findViewById(R.id.btnCn);
        btnMn = (Button) findViewById(R.id.btnMn);
        final Locale curLocale = getResources().getConfiguration().locale;
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        int language = preferences.getInt("language",-1);
        // from 从哪个activity 跳转过来的？
        int from = getIntent().getIntExtra("from",-1);
        if(from ==-1) {
            if (language == -1) {
                languageLyout.setVisibility(View.VISIBLE);
            } else if (language == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent().setClass(SelectLanguageActivity.this, Main1Activity.class));
                        finish();
                    }
                }, 2000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent().setClass(SelectLanguageActivity.this, MainActivity.class));
                        finish();
                    }
                }, 2000);
            }
        }else{
            languageLyout.setVisibility(View.VISIBLE);
        }

        btnCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLang(curLocale.SIMPLIFIED_CHINESE);
                SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("language", 0);
                editor.commit();
                startActivity(new Intent().setClass(SelectLanguageActivity.this, Main1Activity.class));
                finish();
               // restartAPP(getApplicationContext(),0);
            }
        });
        btnMn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLang(curLocale.JAPAN);
                SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("language", 1);
                editor.commit();
                startActivity(new Intent().setClass(SelectLanguageActivity.this, MainActivity.class));
                finish();
             // restartAPP(getApplicationContext(),0);
            }
        });
    }

    private void setLang(Locale locale) {
        // 获得res资源对象
        Resources resources = getResources();
        // 获得设置对象
        Configuration config = resources.getConfiguration();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics dm = resources.getDisplayMetrics();
        // 语言
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }
    private void getPremiss(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (SelectLanguageActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(SelectLanguageActivity.this,permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
    public static void restartAPP(Context context,long Delayed){

        /**开启一个新的服务，用来重启本APP*/
        Intent intent1=new Intent(context,KillSelfService.class);
        intent1.putExtra("PackageName",context.getPackageName());
        intent1.putExtra("Delayed",Delayed);
        context.startService(intent1);

        /**杀死整个进程**/
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    /***重启整个APP*/
    public static void restartAPP(Context context){
        restartAPP(context,2000);
    }
}
