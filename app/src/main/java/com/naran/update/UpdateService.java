package com.naran.update;

/**
 * Created by Menksoft on 2018/1/8.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.naran.weather.R;

import java.io.File;

/**
 * Description:更新下载后台进程
 * User: chenzheng
 * Date: 2016/12/14 0014
 * Time: 16:24
 */
public class UpdateService extends Service{
    private String apkUrl;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;
    @Override
    public void onCreate() {
        Log.e("tag", "UpdateService onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory()+"/toon/toon.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tag", "UpdateService onStartCommand()");
        if(intent==null){
            notifyUser("下载失败！", "下载失败！", 0);
            stopSelf();
        }
        apkUrl = intent.getStringExtra("apkUrl");
        notifyUser("下载中...", "下载中...", 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkUrl, filePath, new UpdateDownloadListener() {
            @Override
            public void onStarted() {
                Log.e("tag", "onStarted()");
            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                Log.e("onProgressChanged", progress+"");

                notifyUser("正在下载...", "正在下载...", progress);
            }

            @Override
            public void onFinished(float completeSize, String downloadUrl) {
                Log.e("tag", "onFinished()");
                notifyUser("下载完成！", "下载完成！", 100);
                stopSelf();
            }

            @Override
            public void onFailure() {
                Log.e("tag", "onFailure()");
                notifyUser("下载失败！", "下载失败！", 0);
                stopSelf();
            }
        });
    }

    /**
     * 更新notification
     * @param result
     * @param msg
     * @param progress
     */
    private void notifyUser(String result, String msg, int progress){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name));
        if(progress>0 && progress<=100){

            builder.setProgress(100,progress,false);

        }else{
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);
        builder.setContentIntent(progress>=100 ? getContentIntent() :
                PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        notification = builder.build();
        notificationManager.notify(0, notification);
    }

    /**
     * 进入apk安装程序
     * @return
     */
    private PendingIntent getContentIntent() {
        File apkFile = new File(filePath);

        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri uri = FileProvider.getUriForFile(this, this.getPackageName()+".fileProvider", apkFile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, install, PendingIntent.FLAG_UPDATE_CURRENT);
            startActivity(install);
            return pendingIntent;
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, install, PendingIntent.FLAG_UPDATE_CURRENT);
            startActivity(install);
            return pendingIntent;
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
