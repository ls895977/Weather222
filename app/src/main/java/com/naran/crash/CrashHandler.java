package com.naran.crash;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.naran.weather.MainActivity;
import com.naran.weather.WeatherApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MENK021 on 2017/1/18.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * TAG
     */
    private static final String TAG = "CrashHandler";
    SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * uploadUrl
     * 服务器的地址，根据自己的情况进行更改
     **/
//    private static final String uploadUrl = "http://3.saymagic.sinaapp.com/ReceiveCrash.php";

    /**
     * localFileUrl
     * 本地log文件的存放地址
     **/
    private static String localFileUrl = "";

    /**
     * mDefaultHandler
     */
    private Thread.UncaughtExceptionHandler defaultHandler;

    /**
     * instance
     */
    private static CrashHandler instance = new CrashHandler();

    /**
     * infos
     */
    private Map<String, String> infos = new HashMap<String, String>();

    /** formatter */
//    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * context
     */
    private WeatherApplication context;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * @param ctx 初始化，此处最好在Application的OnCreate方法里来进行调用
     */
    public void init(WeatherApplication ctx) {
        this.context = ctx;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException
     * 在这里处理为捕获的Exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handleException(throwable);
        defaultHandler.uncaughtException(thread, throwable);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        Log.d("TAG", "收到崩溃");
        collectDeviceInfo(context);
        writeCrashInfoToFile(ex);
        restart();
        return true;
    }

    /**
     * @param ctx 手机设备相关信息
     */
    public void collectDeviceInfo(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("crashTime", formatter.format(new Date()));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * @param ex 将崩溃写入文件系统
     */
    private void writeCrashInfoToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        //这里把刚才异常堆栈信息写入SD卡的Log日志里面
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcardPath = Environment.getExternalStorageDirectory().getPath();
            String filePath = sdcardPath + "/toonQagoor/crash/";
            localFileUrl = writeLog(sb.toString(), filePath);
        }
    }

    /**
     * @param log
     * @param name
     * @return 返回写入的文件路径
     * 写入Log信息的方法，写入到SD卡里面
     */
    private String writeLog(String log, String name) {
        CharSequence timestamp = new Date().toString().replace(" ", "");
        timestamp = "crash";
        String filename = name + timestamp + ".log";

        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            Log.d("TAG", "写入到SD卡里面");
            //            FileOutputStream stream = new FileOutputStream(new File(filename));
            //            OutputStreamWriter output = new OutputStreamWriter(stream);
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            fw.close();
            return filename;
        } catch (IOException e) {
            Log.e(TAG, "an error occured while writing file...", e);
            e.printStackTrace();
            return null;
        }
    }

    private void restart() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        }
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent,
                Intent.FILL_IN_ACTION);
        //退出程序
//        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                restartIntent); // 1秒钟后重启应用
    }

}
