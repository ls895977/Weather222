package com.naran.notify;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.naran.Utils.AudioUtils.AudioRecordButton;
import com.naran.Utils.AudioUtils.MediaManager;
import com.naran.Utils.AudioUtils.PermissionHelper;
import com.naran.Utils.AudioUtils.Record;
import com.naran.Utils.LoginUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.interfaces.OnScreenShootListener;
import com.naran.interfaces.OnScreenShootTask;
import com.naran.useraccount.LoginActivity;
import com.naran.weather.R;

import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.app.Activity.RESULT_OK;


public class NotifyFragment extends Fragment implements OnScreenShootListener,BDLocationListener {
    private View view;
    private ImageView disaster;
    private Bitmap bmp;
    private ImageView call, notify1, notify2, notify4, notify5, notify6;
    private int disnum = 0;
    private Button submit;
    private LinearLayout line1, line2, line3;
    private LinearLayout line11, line22, line33;
    private EditText edittext1, edittext2, edittext3, edittext5, edittext6, edittext7;
    private EditText position;
    private Handler handler;
    LocationClient mLocationClient = null;
    private String ids ="";
    private int postedImgCount = 0;
    private String tempPath="";

    private AudioRecordButton mEmTvBtn;
    private PermissionHelper mHelper;
    private String voicIDs = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notify, container, false);
        initView();
        initListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
        }
        handler = new Handler();
        initEvent();

        mLocationClient = new LocationClient(getActivity());
        //声明LocationClient类
        mLocationClient.registerLocationListener( this );
        initLocation();
        mLocationClient.start();
        return view;
    }

    private void initEvent() {
        disaster.setOnClickListener(new OnClick());
        call.setOnClickListener(new OnClick());
        submit.setOnClickListener(new OnClick());
    }

    private void initView() {
        mEmTvBtn = (AudioRecordButton) view.findViewById(R.id.em_tv_btn);
        line1 = (LinearLayout) view.findViewById(R.id.line1);
        line2 = (LinearLayout) view.findViewById(R.id.line2);
        line3 = (LinearLayout) view.findViewById(R.id.line3);
        line11 = (LinearLayout) view.findViewById(R.id.line11);
        line22 = (LinearLayout) view.findViewById(R.id.line22);
        line33 = (LinearLayout) view.findViewById(R.id.line33);
        submit = (Button) view.findViewById(R.id.button_notify_submit);
        disaster = (ImageView) view.findViewById(R.id.disaster);
        call = (ImageView) view.findViewById(R.id.imageView_notify_call);
        notify1 = (ImageView) view.findViewById(R.id.imageView_notify_1);
        notify2 = (ImageView) view.findViewById(R.id.imageView_notify_2);
        notify4 = (ImageView) view.findViewById(R.id.imageView_notify_4);
        notify5 = (ImageView) view.findViewById(R.id.imageView_notify_5);
        notify6 = (ImageView) view.findViewById(R.id.imageView_notify_6);
        edittext1 = (EditText) view.findViewById(R.id.edittext_notify_1);
        edittext2 = (EditText) view.findViewById(R.id.edittext_notify_2);
        edittext3 = (EditText) view.findViewById(R.id.edittext_notify_3);
        edittext5 = (EditText) view.findViewById(R.id.edittext_notify_5);
        edittext6 = (EditText) view.findViewById(R.id.edittext_notify_6);
        edittext7 = (EditText) view.findViewById(R.id.edittext_notify_7);
        position = (EditText)view.findViewById(R.id.imageView_notify_position);
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("mode",1);
//                intent.setClass(getActivity(), MapActivity.class);
//                startActivity(intent);

            }
        });
        setLineWidth();
    }

    private void setLineWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        line1.getLayoutParams().width = width / 3;
        line2.getLayoutParams().width = width / 3;
        line3.getLayoutParams().width = width / 3;
        line11.getLayoutParams().width = width / 3;
        line22.getLayoutParams().width = width / 3;
        line33.getLayoutParams().width = width / 3;
    }

    public void showImagePickDialog() {
        String title = "选取照片";
        String[] items = new String[]{"相机", "相册"};
        new AlertDialog.Builder(getActivity()).setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                // ѡ������
                                pickImageFromCamera();
                                break;
                            case 1:
                                // ѡ�����
                                pickImageFromAlbum();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    public void pickImageFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = new File(Environment.getExternalStorageDirectory() + "/toon");
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        tempPath = Environment.getExternalStorageDirectory() + "/toon/"+ System.currentTimeMillis() +"toon.jpg";
        File file = new File(tempPath);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            // 从文件中创建uri
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = getActivity().getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, 2);
    }

    /*
     * ��ϵͳ���
     */
    public void pickImageFromAlbum() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent1, 1);
    }

    public int[] getScreenSize() {
        //返回当前屏幕宽[0]高[1]
        int[] size = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        size[0] = width;
        size[1] = height;
        return size;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri originalUri = data.getData();  //获得图片的uri

                    ContentResolver resolver = getActivity().getContentResolver();
                    try {
                        Bitmap  bmp = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if(bmp!=null){
                            if (disnum == 0) {
                                notify1.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 1) {
                                notify2.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 2) {
                                notify4.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 3) {
                                notify5.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 4) {
                                notify6.setImageBitmap(bmp);// ������SD����
                            }
                            disnum++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(tempPath);
                    Uri originalUri = Uri.fromFile(temp);// �ü�ͼƬ
                    ContentResolver resolver = getActivity().getContentResolver();
                    try {
                        Bitmap  bmp = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if(bmp!=null){
                            if (disnum == 0) {
                                notify1.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 1) {
                                notify2.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 2) {
                                notify4.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 3) {
                                notify5.setImageBitmap(bmp);// ������SD����
                            } else if (disnum == 4) {
                                notify6.setImageBitmap(bmp);// ������SD����
                            }
                            disnum++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    bmp = extras.getParcelable("data");
                    if (bmp != null) {
                        if (disnum == 0) {
                            notify1.setImageBitmap(bmp);// ������SD����
                        } else if (disnum == 1) {
                            notify2.setImageBitmap(bmp);// ������SD����
                        } else if (disnum == 2) {
                            notify4.setImageBitmap(bmp);// ������SD����
                        } else if (disnum == 3) {
                            notify5.setImageBitmap(bmp);// ������SD����
                        } else if (disnum == 4) {
                            notify6.setImageBitmap(bmp);// ������SD����
                        }
                        disnum++;
                    }
                }
                break;
        }
    }
    private void postImage() {
        if(disnum>0) {
            notify1.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = notify1.getDrawingCache();
            Map<String, String> map = new HashMap<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            byte[] bytes = bos.toByteArray();
            String base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
            map.put("btyestr", base64Img);
            map.put("imgname", "darhad"+System.currentTimeMillis()+".png");
            map.put("Position", "123-456");
            OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject jb = new JSONObject(result);

                    if(ids.equals("")){
                        ids = jb.optString("ImgID");
                    }else{
                        ids+=","+jb.optString("ImgID");
                    }
                    postedImgCount++;
                    notify1.setDrawingCacheEnabled(false);
                    if(postedImgCount == disnum){
                        sendMsg();
                    }
                }
            });
        }
        if(disnum>1) {
            notify2.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = notify2.getDrawingCache();

            Map<String, String> map = new HashMap<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            byte[] bytes = bos.toByteArray();
            String base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
            map.put("btyestr", base64Img);
            map.put("imgname", "darhad"+System.currentTimeMillis()+".png");
            map.put("Position", "123-456");
            OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject jb = new JSONObject(result);

                    if(ids.equals("")){
                        ids = jb.optString("ImgID");
                    }else{
                        ids+=","+jb.optString("ImgID");
                    }
                    postedImgCount++;
                    notify2.setDrawingCacheEnabled(false);
                    if(postedImgCount == disnum){

                        sendMsg();
                    }
                }
            });
        }
        if(disnum>2) {
            notify4.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = notify4.getDrawingCache();
            Map<String, String> map = new HashMap<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            byte[] bytes = bos.toByteArray();
            String base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
            map.put("btyestr", base64Img);
            map.put("imgname", "darhad"+System.currentTimeMillis()+".png");
            map.put("Position", "123-456");
            OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject jb = new JSONObject(result);

                    if(ids.equals("")){
                        ids = jb.optString("ImgID");
                    }else{
                        ids+=","+jb.optString("ImgID");
                    }
                    postedImgCount++;
                    notify4.setDrawingCacheEnabled(false);
                    if(postedImgCount == disnum){

                        sendMsg();
                    }
                }
            });
        }
        if(disnum>3) {
            notify5.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = notify5.getDrawingCache();

            Map<String, String> map = new HashMap<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            byte[] bytes = bos.toByteArray();
            String base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
            map.put("btyestr", base64Img);
            map.put("imgname", "darhad"+System.currentTimeMillis()+".png");
            map.put("Position", "123-456");
            OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject jb = new JSONObject(result);

                    if(ids.equals("")){
                        ids = jb.optString("ImgID");
                    }else{
                        ids+=","+jb.optString("ImgID");
                    }
                    postedImgCount++;
                    notify5.setDrawingCacheEnabled(false);
                    if(postedImgCount == disnum){

                        sendMsg();
                    }
                }
            });
        }
        if(disnum>4) {
            notify6.setDrawingCacheEnabled(true);
            Bitmap bitmap1 = notify6.getDrawingCache();

            Map<String, String> map = new HashMap<>();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            byte[] bytes = bos.toByteArray();
            String base64Img = Base64.encodeToString(bytes, Base64.DEFAULT);
            map.put("btyestr", base64Img);
            map.put("imgname", "darhad"+System.currentTimeMillis()+".png");
            map.put("Position", "123-456");
            OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    JSONObject jb = new JSONObject(result);

                    if(ids.equals("")){
                        ids = jb.optString("ImgID");
                    }else{
                        ids+=","+jb.optString("ImgID");
                    }
                    postedImgCount++;
                    notify6.setDrawingCacheEnabled(false);
                    if(postedImgCount == disnum){
                        sendMsg();
                    }
                }
            });
        }
    }

    private void sendMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("DeathNumber", edittext1.getText().toString());
        map.put("InjuredNumber", edittext2.getText().toString());
        map.put("InfluenceNumber", edittext3.getText().toString());
        map.put("HouseDamaged", edittext5.getText().toString());
        map.put("livestockDamaged", edittext6.getText().toString());
        map.put("OtherInfluence", edittext7.getText().toString());
        map.put("FullName", LoginUtil.getInstance().userInfoModel.getUserName());
        map.put("PhoneNumber", LoginUtil.getInstance().userInfoModel.getUserName());
        map.put("Position", position.getText().toString()+"|"+ids);
        OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AAddDisasterReporting", map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                Log.e("requestFailure", request.toString());
            }
            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject jb = new JSONObject(result);
                notify1.setImageDrawable(null);
                notify2.setImageDrawable(null);
                notify4.setImageDrawable(null);
                notify5.setImageDrawable(null);
                notify6.setImageDrawable(null);
                disnum = 0;
                postedImgCount = 0;
                edittext1.setText("");
                edittext2.setText("");
                edittext3.setText("");
                edittext5.setText("");
                edittext6.setText("");
                edittext7.setText("");
                if(jb.optBoolean("result")){
                    Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void setOnScreenShootListener(Bitmap bmp) {
        if(null!=bmp){
//            position.setImageBitmap(bmp);
//            postImage(bitmapToBase64(bmp));
        }
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.disaster:
                    if (disnum < 5) {
                        showImagePickDialog();
                    } else {
                        Toast.makeText(getContext(), "照片够了", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imageView_notify_call:
                    showCall();
                    break;
                case R.id.button_notify_submit:
                    if(!LoginUtil.getInstance().isLogined){
                        startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                    }else {
                        if ((edittext1.getText().toString().trim().equals("")) ||
                                (edittext2.getText().toString().trim().equals("")) ||
                                (edittext3.getText().toString().trim().equals("")) ||
                                (edittext5.getText().toString().trim().equals("")) ||
                                (edittext6.getText().toString().trim().equals("")) ||
                                (edittext7.getText().toString().trim().equals(""))) {
                       // Toast.makeText(getActivity(), "信息不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            postImage();
                        }
                    }
                    break;
            }
        }
    }

    private void showCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "04792242891"));
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OnScreenShootTask.getInstance().removeScreenShootListener(this);
        System.gc();
    }
    @Override
    public void onReceiveLocation(BDLocation location) {
        //获取定位结果
        final StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(location.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度
        String address = "";
        if (location.getLocType() == BDLocation.TypeGpsLocation){

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息
            address = location.getAddrStr();
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
            address = location.getAddrStr();

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
            address = location.getAddrStr();
        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }
        final String add = address;
        handler.post(new Runnable() {
            @Override
            public void run() {
                position.setText(add);
            }
        });
        Log.i("BaiduLocationApiDem", sb.toString());
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }
    private void initListener() {
        mEmTvBtn.setHasRecordPromission(false);
//        授权处理
        mHelper = new PermissionHelper(this);

        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音",
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        mEmTvBtn.setHasRecordPromission(true);
                        mEmTvBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
                            @Override
                            public void onFinished(float seconds, String filePath) {
                                // 录音
                                Record recordModel = new Record();
                                recordModel.setSecond((int) seconds <= 0 ? 1 : (int) seconds);
                                recordModel.setPath(filePath);
                                recordModel.setPlayed(false);
                                // 录音结束
                                ///// 音频文件发送到后台
                                Map<String, String> map = new HashMap<>();
                                try {
                                    map.put("btyestr", voic2Base64File(filePath));

                                    map.put("imgname", "voic" + System.currentTimeMillis() + ".amr");
                                    map.put("Position", "123-456");
                                    OkHttpUtil.postAsync("http://121.41.123.152:8088/interface.asmx/AUploadImgs", map, new OkHttpUtil.DataCallBack() {
                                        @Override
                                        public void requestFailure(Request request, IOException e) {
                                            Toast.makeText(getActivity(), request.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void requestSuccess(String result) throws Exception {
                                            JSONObject jb = new JSONObject(result);

                                            if (voicIDs.equals("")) {
                                                voicIDs = jb.optString("ImgID");
                                            } else {
                                                voicIDs += "," + jb.optString("ImgID");
                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mEmTvBtn.setHasRecordPromission(false);
                        Toast.makeText(getActivity(), "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPause() {
        MediaManager.release();//保证在退出该页面时，终止语音播放
        super.onPause();
    }

    public static String voic2Base64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
}
