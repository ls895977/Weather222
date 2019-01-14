package com.naran.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.naran.weather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 创建时间：2017/4/29
 * 作者：amin
 * 功能：
 */

public class UpdateManager {


    //下载中...
    private static final int DOWNLOAD = 1;
    //下载完成
    private static final int DOWNLOAD_FINISH = 2;
    //保存解析的XML信息

    //下载保存路径
    private String mSavePath;
    //记录进度条数量
    private int progress;
    //是否取消更新
    private boolean cancelUpdate = false;
    //上下文对象
    private Context mContext;
    //进度条
    private ProgressBar mProgressBar;
    //更新进度条的对话框
    private Dialog mDownloadDialog;
    private String myurl;


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                //下载中。。。
                case DOWNLOAD:
                    //更新进度条
                    System.out.println(progress);
                    mProgressBar.setProgress(progress);
                    break;
                //下载完成
                case DOWNLOAD_FINISH:
                    // 安装文件
//                    installApk();
                    installAPK(Uri.parse("file://" + mSavePath+"/taonao.apk".toString()),mContext);
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        super();
        this.mContext = context;
    }


    /**
     * 检测软件更新
     */

    public void showNoticeDialog(String url) {
        // TODO Auto-generated method stub
        this.myurl = url;
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("发现新版本，现在更新！");
        //更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("下次", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在更新。。。");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.softupdate_progress, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.update_progress);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        //下载文件

        downloadApk();
    }

    /**
     * 下载APK文件
     */
    private void downloadApk() {
        // TODO Auto-generated method stub
        // 启动新线程下载软件
        new DownloadApkThread().start();
    }


    /**
     * 检查软件是否有更新版本
     *
     * @return
     */


    /**
     * 下载文件线程
     *
     * @author Administrator
     */
    private class DownloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                //判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获取SDCard的路径

                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(myurl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 如果文件不存在，新建目录
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(mSavePath, "taonao.apk");

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条的位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);//点击取消就停止下载
                    fos.close();
                    is.close();
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "请给app读取SDCard权限，否则无法下载！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

//    /**
//     * 安装APK文件
//     */
//    private void installApk() {
//        File apkfile = new File(mSavePath, "taonao.apk");
//        if (!apkfile.exists()) {
//            return;
//        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        mContext.startActivity(i);
//    }
    private void installAPK(Uri apk, Context context) {
        if (Build.VERSION.SDK_INT < 23) { //6.0以下
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intents);
        } else if(Build.VERSION.SDK_INT ==23){ //6.0
            File file = new File(mSavePath+ "/taonao.apk");
            if (file.exists()) {
                openFile(file, context);
            }
        }else{ // 7.0以上
            File file = new File(mSavePath+ "/taonao.apk");
          //  installApkFile(context,file);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(install);
        }
    }
    public static void installApkFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.naran.weather.GenericFileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }
    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
