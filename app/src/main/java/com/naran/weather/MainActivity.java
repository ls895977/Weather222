package com.naran.weather;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.naran.HomPage.HomePageFragment;
import com.naran.Utils.Utils;
import com.naran.connector.OkHttpUtil;
import com.naran.controls.NaranButton;
import com.naran.feedback.FeedBackFragment;
import com.naran.menu.MenuFragment;
import com.naran.notify.NotifyFragment;

import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Request;

public class MainActivity extends FragmentActivity {

    private FrameLayout frameLayout;
    private HomePageFragment homePageFragment;
    private FeedBackFragment feedBackFragment;
    private NotifyFragment notifyFragment;
    private MenuFragment mineFragment;
    private int versionCode = -1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private NaranButton home, feed, notify, mine;
    private String pkgName;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logStringCache = Utils.getLogText(getApplicationContext());
        pkgName = this.getPackageName();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getPromiss();
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("onClick","onClick");
//            }
//        });
        initViews();
        isCanUpdate();
    }

    private void initViews() {
        frameLayout = (FrameLayout) findViewById(R.id.fragmentLayout);
        fragmentManager = getSupportFragmentManager();
        homePageFragment = new HomePageFragment();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, homePageFragment).commit();

        home = (NaranButton) findViewById(R.id.home);
        feed = (NaranButton) findViewById(R.id.feed);
        notify = (NaranButton) findViewById(R.id.notify);
        mine = (NaranButton) findViewById(R.id.mine);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setTextColor(Color.WHITE);
                feed.setTextColor(Color.YELLOW);
                notify.setTextColor(Color.YELLOW);
                mine.setTextColor(Color.YELLOW);
                fragmentTransaction = fragmentManager.beginTransaction();
                if (feedBackFragment != null) {
                    fragmentTransaction.hide(feedBackFragment);
                }
                if (notifyFragment != null) {
                    fragmentTransaction.hide(notifyFragment);
                }
                if (mineFragment != null) {
                    fragmentTransaction.hide(mineFragment);
                }
                fragmentTransaction.show(homePageFragment);
                fragmentTransaction.commit();
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setTextColor(Color.YELLOW);
                feed.setTextColor(Color.WHITE);
                notify.setTextColor(Color.YELLOW);
                mine.setTextColor(Color.YELLOW);

                fragmentTransaction = fragmentManager.beginTransaction();

                if (feedBackFragment != null) {
                    fragmentTransaction.show(feedBackFragment);
                } else {
                    feedBackFragment = new FeedBackFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, feedBackFragment);
                    fragmentTransaction.show(feedBackFragment);
                }
                if (notifyFragment != null) {
                    fragmentTransaction.hide(notifyFragment);
                }
                if (mineFragment != null) {
                    fragmentTransaction.hide(mineFragment);
                }
                fragmentTransaction.hide(homePageFragment);
                fragmentTransaction.commit();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setTextColor(Color.YELLOW);
                feed.setTextColor(Color.YELLOW);
                notify.setTextColor(Color.WHITE);
                mine.setTextColor(Color.YELLOW);
                fragmentTransaction = fragmentManager.beginTransaction();
                if (notifyFragment != null) {
                    fragmentTransaction.show(notifyFragment);
                } else {
                    notifyFragment = new NotifyFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, notifyFragment);
                    fragmentTransaction.show(notifyFragment);
                }
                if (feedBackFragment != null) {
                    fragmentTransaction.hide(feedBackFragment);
                }
                if (mineFragment != null) {
                    fragmentTransaction.hide(mineFragment);
                }
                fragmentTransaction.hide(homePageFragment);
                fragmentTransaction.commit();
            }
        });
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setTextColor(Color.YELLOW);
                feed.setTextColor(Color.YELLOW);
                notify.setTextColor(Color.YELLOW);
                mine.setTextColor(Color.WHITE);
                fragmentTransaction = fragmentManager.beginTransaction();
                if (mineFragment != null) {
                    fragmentTransaction.show(mineFragment);
                } else {
                    mineFragment = new MenuFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, mineFragment);
                    fragmentTransaction.show(mineFragment);
                }
                if (feedBackFragment != null) {
                    fragmentTransaction.hide(feedBackFragment);
                }
                if (notifyFragment != null) {
                    fragmentTransaction.hide(notifyFragment);
                }
                if (feedBackFragment != null) {
                    fragmentTransaction.hide(feedBackFragment);
                }
                fragmentTransaction.hide(homePageFragment);
                fragmentTransaction.commit();
            }
        });
// 启动百度push
        //     if(!PushManager.isPushEnabled(getApplicationContext()))
//            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
//                    Utils.getMetaValue(MainActivity.this, "api_key"));
        //   PushManager.stopWork(getApplicationContext());
//        PushManager.stopWork(getApplicationContext());
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
//        Resources resource = this.getResources();
//        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
//                resource.getIdentifier(
//                        "notification_custom_builder", "layout", pkgName),
//                resource.getIdentifier("share", "id", pkgName),
//                resource.getIdentifier("share", "id", pkgName),
//                resource.getIdentifier("share", "id", pkgName));
//        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
//        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
//        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
//        cBuilder.setLayoutDrawable(resource.getIdentifier(
//                "share", "drawable", pkgName));
//        cBuilder.setNotificationSound(Uri.withAppendedPath(
//                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
//        // 推送高级设置，通知栏样式设置为下面的ID
//        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }

    public String getIdentity() {
        SharedPreferences preference = getSharedPreferences("identity", Context.MODE_PRIVATE);
        String identity = preference.getString("identity", null);
        if (identity == null) {
            identity = java.util.UUID.randomUUID().toString();
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("identity", identity);
            editor.commit();
        }
        return identity;
    }

    private void isCanUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            OkHttpUtil.getAsync(OkHttpUtil.update, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {

                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    int onLineVersionCode = Integer.parseInt(new JSONObject(result).optString("EditionNo"));
                    if (versionCode != -1 && onLineVersionCode > versionCode) {
//                        postNotification(new JSONObject(result).optString("DownloadUrl"));
                        dialog(new JSONObject(result).optString("DownloadUrl"));
//                        UpdateManager updateManager = new UpdateManager(MainActivity.this);
//                        updateManager.showNoticeDialog(new JSONObject(result).optString("DownloadUrl"));

                    }
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void dialog(final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("检测到更新，是否更新？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(downloadUrl);
                intent.setData(content_url);
                startActivity(intent);
//                Intent intent = new Intent(MainActivity.this, UpdateService.class);
//                intent.putExtra("apkUrl", downloadUrl);
//                startService(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void postNotification(String downloadUrl) {
        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(downloadUrl);
//        intent.setData(content_url);
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra("downloadUrl", downloadUrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("套瑙天气检测到更新");
        builder.setContentText("点击更新到最新版本");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setTicker("套瑙天气更新通知");
        builder.setOngoing(true);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
//        notification.flags = Notification.FLAG_NO_CLEAR;  //ֻ��ȫ�����ʱ��Notification�Ż����
        if (notification != null) {
            notificationManager.notify(0, notification);
        }
    }

    private void getPromiss() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (MainActivity.this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
}
