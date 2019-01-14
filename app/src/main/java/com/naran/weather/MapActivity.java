package com.naran.weather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.naran.controls.NaranButton;
import com.naran.interfaces.OnScreenShootTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MapActivity extends Activity implements BaiduMap.OnMarkerClickListener{
    private RelativeLayout screenLayout;
    private BMapManager bMapManager = null;
    private MapView mMapView = null;
    // 百度地图对象
    private BaiduMap bdMap;
    private LocationClient mLocClient;
    private MyLocationListenner myListener;
    private NaranButton myPosition;
    private boolean IsFirstLoc = true;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    int mode = -1;
    private NaranButton btnSend;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        handler = new Handler();
        SDKInitializer.initialize(getApplicationContext());
        MapView.setMapCustomEnable(false);
        setMapCustomFile(this);
//      mMapView = new MapView(this, new BaiduMapOptions());
        setContentView(R.layout.activity_map);
        screenLayout = (RelativeLayout)findViewById(R.id.screenLayout);
        mode =  getIntent().getIntExtra("mode",-1);
        btnSend = (NaranButton)findViewById(R.id.send);
        if(mode!=-1){
            btnSend.setVisibility(View.VISIBLE);
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                screenLayout.setDrawingCacheEnabled(true);
//                Bitmap bmp = screenLayout.getDrawingCache();


                mMapView.getMap().snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(final Bitmap bitmap) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                OnScreenShootTask.getInstance().fireMsg(bitmap);
                                finish();
                            }
                        });
                    }
                });
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mMapView = (MapView)findViewById(R.id.mapView);
        bdMap = mMapView.getMap();
        //截屏
        ///
        bdMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        bdMap.setOnMarkerClickListener(this);
        // 开启定位图层
        bdMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(500);
        option.setIsNeedAddress(true);
        bdMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(14).build()));
        mLocClient.setLocOption(option);
        mLocClient.start();

        myPosition = (NaranButton)findViewById(R.id.myposition);
        myPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BDLocation location = mLocClient.getLastKnownLocation();
                if (location != null) {
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    bdMap.animateMapStatus(u);
                }
            }
        });

    }
    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/custom_config.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_config.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/custom_config.txt");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        public void onReceivePoi(BDLocation poiLocation) {

        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation == null || mMapView == null) {
                return;
            }
            if(bdLocation.getLatitude()==0||bdLocation.getLongitude()==0){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            if(IsFirstLoc) {
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                bdMap.animateMapStatus(u);
                IsFirstLoc = false;
            }
            bdMap.setMyLocationData(locData);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
