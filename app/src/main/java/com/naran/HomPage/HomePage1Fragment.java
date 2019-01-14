package com.naran.HomPage;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.naran.Utils.LoginUtil;
import com.naran.addresmanager1.AddressManager1Activity;
import com.naran.addresmanager1.AddressChangeListener;
import com.naran.addresmanager1.AddressChangeTask;
import com.naran.addressmanager.TextArticleTitle;
import com.naran.connector.OkHttpUtil;
import com.naran.controls.CnArticalView;
import com.naran.controls.CnArticalView5;
import com.naran.controls.GifView;
import com.naran.controls.MnTextView;
import com.naran.controls.NaranButton;
import com.naran.controls.RealChineseTimeWeather;
import com.naran.controls.TextAndImageView1;
import com.naran.controls.WeekChineseWeather;
import com.naran.model.AreaModel;
import com.naran.model.RealTimeWeatherModel;
import com.naran.model.WarnListInfoModel;
import com.naran.model.WeekWeatherModel_Chinese;
import com.naran.model.WranAndServiceModel;
import com.naran.route.RouteActivity;
import com.naran.useraccount.LoginActivity;
import com.naran.weather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomePage1Fragment extends Fragment implements AddressChangeListener{

    private GifView gifView;
    private Map<String, String> map = new HashMap<>();
    private View view;
    private List<AreaModel> areaModels;
    List<WranAndServiceModel> wranAndServiceModels;
    private NaranButton btnLocation;
    private LinearLayout articalLayout;
    private LinearLayout weekLayout;
    private RelativeLayout baseLayout;
    private LinearLayout baseContentLayout;
    private LinearLayout majorWeatherLayout;
    private NaranButton btnMap;
    private PopupWindow mPopupWindow;// 一级地区选择
    private PopupWindow mPopupWindow1;// 二级地区选择
    private WeekChineseWeather weekWeather;
    private TextView address;
    private TextView wet, fire, direction, temp, weather, phonominon;
    private ProgressBar mPBar;
    private TextView titile, content, date;
    private ImageView imageView_weather1;
    private RelativeLayout mRelaWarning;
    private LinearLayout line_weather_fire;
    private TextView dateTime;
    private TextAndImageView1 textAndImageView;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_page1, container, false);
        initViews();
        AddressChangeTask.getInstance().addOnAddressChangeListener(this);
        getArea();
        map.put("type", "cn_word");
        map.put("TimeNumber", "1");
        handler = new Handler();
        getWeekDatas();
        getWarnAndServiceByCondition();
        return view;
    }

    private void initViews() {
        gifView = (GifView)view.findViewById(R.id.gifView);
        line_weather_fire = (LinearLayout) view.findViewById(R.id.line_weather_fire);
        line_weather_fire.setVisibility(View.GONE);
        mRelaWarning = (RelativeLayout) view.findViewById(R.id.rela_weather);
        mRelaWarning.setVisibility(View.GONE);
        imageView_weather1 = (ImageView) view.findViewById(R.id.imageView_weather1);
        mPBar = (ProgressBar) view.findViewById(R.id.progressbar_homepage1);
        mPBar.setVisibility(View.GONE);
        dateTime = (TextView)view.findViewById(R.id.dateTime);
        wet = (TextView) view.findViewById(R.id.TextView_wet1);
        fire = (TextView) view.findViewById(R.id.TextView_fire1);
        direction = (TextView) view.findViewById(R.id.TextView_direction1);
        temp = (TextView) view.findViewById(R.id.textView_temp);
        weather = (TextView) view.findViewById(R.id.textView_weather);
        titile = (TextView) view.findViewById(R.id.mnTextView_Phonominon1);
        content = (TextView) view.findViewById(R.id.textView_weather1);
        date = (TextView) view.findViewById(R.id.textview_weather_date);
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        areaModels = new ArrayList<>();
        OkHttpUtil.getInstance();
        address = (TextView) view.findViewById(R.id.address);
        OkHttpUtil.getAsync(OkHttpUtil.AGetAllAreaOne, new OkHttpUtil.DataCallBack() {
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), "获取地区数据失败！", Toast.LENGTH_SHORT).show();
            }
            public void requestSuccess(String result) throws Exception {
                List<AreaModel> areaModels = new ArrayList<AreaModel>();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                // popupwindow
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.view_area_popup, null);
                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popupLayout);
                for (int i = 0; i < dataArray.length(); i++) {
                    final AreaModel areaModel = new AreaModel(dataArray.optJSONObject(i));
                    areaModels.add(areaModel);
                    MnTextView mnTextView = new MnTextView(getActivity());
                    if (i==0){
                        mnTextView.setText(" ☆ "+areaModel.getCHineseAreaName());
                    }else {
                        mnTextView.setText("\n ☆ "+areaModel.getCHineseAreaName());
                    }
                    mnTextView.setTextColor(Color.BLUE);
                    mnTextView.setTextSize(Dp2Px(5));
                    mnTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                                mPopupWindow.dismiss();
                            }
                            getSecoundAddress(areaModel.getAreaID()+"");
                        }
                    });
                    popupLayout.addView(mnTextView);
                }
                mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mPopupWindow.setTouchable(true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            }
        });
        btnMap = (NaranButton) view.findViewById(R.id.mapBtn);
        baseLayout = (RelativeLayout) view.findViewById(R.id.baseLayout);
        btnLocation = (NaranButton) view.findViewById(R.id.location);
        baseContentLayout = (LinearLayout) view.findViewById(R.id.baseContentLayout);
        weekLayout = (LinearLayout) view.findViewById(R.id.realWeatherLayout);
        btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RouteActivity.class);
                startActivity(intent);
            }
        });
        wranAndServiceModels= new ArrayList<WranAndServiceModel>();
        articalLayout = (LinearLayout) view.findViewById(R.id.articalLayout);
        initWeatherView();
        textAndImageView = new TextAndImageView1(getActivity());
        majorWeatherLayout = (LinearLayout) view.findViewById(R.id.majorWeatherLayout);

//////////// 专业气象服务
        OkHttpUtil.getAsync(OkHttpUtil.AGetService, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), "未能获取专业服务信息", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                if(null!=result){

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for(int i=0;i<dataArray.length();i++){

                        JSONObject jobj = dataArray.optJSONObject(i);
                        if(i==0){
                            textAndImageView.imgUrl1 = jobj.optString("ImgUrl");
                            textAndImageView.getTvFirs().setText(jobj.optString("ChinaContent"));
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
                            Request request = new Request.Builder()
                                    .url(jobj.optString("ImgUrl"))
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
                                    BitmapFactory.Options opts = new BitmapFactory.Options();
                                    opts.inSampleSize = 2;
                                    final Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length,opts);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            textAndImageView.getImgFirst().setImageBitmap(bitmap);

                                        }
                                    });
                                }
                            });
                        }
                        if(i==1){
                            textAndImageView.getTvSecond().setText(jobj.optString("ChinaContent"));
                            textAndImageView.imgUrl2 = jobj.optString("ImgUrl");
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
                            Request request = new Request.Builder()
                                    .url(jobj.optString("ImgUrl"))
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
                                    //通过imageview，设置图片
                                    BitmapFactory.Options opts = new BitmapFactory.Options();
                                    opts.inSampleSize = 2;
                                    final Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length,opts);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            textAndImageView.getImgSecond().setImageBitmap(bitmap);
                                        }
                                    });
                                }
                            });
                        }
                        if(i==2){
                            textAndImageView.getTvThird().setText(jobj.optString("ChinaContent"));
                            textAndImageView.imgUrl3 = jobj.optString("ImgUrl");
                            OkHttpClient okHttpClient = new OkHttpClient();
                            //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
                            Request request = new Request.Builder()
                                    .url(jobj.optString("ImgUrl"))
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
                                    BitmapFactory.Options opts = new BitmapFactory.Options();
                                    opts.inSampleSize = 2;
                                    final Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length,opts);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            textAndImageView.getImgThird().setImageBitmap(bitmap);
                                        }
                                    });

                                }
                            });
                        }
                    }
                    majorWeatherLayout.addView(textAndImageView);
                }else{
                    majorWeatherLayout.setVisibility(View.GONE);
                }

            }
        });
        weekLayout = (LinearLayout) view.findViewById(R.id.weekWeatherLayout);
        weekWeather = new WeekChineseWeather(getActivity());
        weekLayout.addView(weekWeather);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!LoginUtil.getInstance().isLogined){
                    startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                }else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), AddressManager1Activity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LoginUtil.getInstance().isLogined){
                    startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                }else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), AddressManager1Activity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void initWeatherView() {
        RealChineseTimeWeather realTimeWeather = new RealChineseTimeWeather(getActivity());
        weekLayout.addView(realTimeWeather);
    }

    public void getWeekDatas() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPBar.setVisibility(View.VISIBLE);
            }
        });
        OkHttpUtil.postAsync(OkHttpUtil.GetWeatherForecast, map, new OkHttpUtil.DataCallBack() {
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            public void requestSuccess(String result) throws Exception {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                List<WeekWeatherModel_Chinese> weekWeatherModels = new ArrayList<WeekWeatherModel_Chinese>();
                for (int i = 0; i < dataArray.length(); i++) {
                    WeekWeatherModel_Chinese weekWeatherModel = new WeekWeatherModel_Chinese(dataArray.optJSONObject(i));
                    weekWeatherModels.add(weekWeatherModel);
                }
                weekWeather.setDatas(weekWeatherModels);
                getRealTimeWeek();
            }
        });
    }
    public void getWarnAndServiceByCondition() {
        Map<String,String> serviceMap = new HashMap<>();
        serviceMap.put("serviceType","3");
        serviceMap.put("WarningAndServiceID","0");
        OkHttpUtil.postAsync(OkHttpUtil.GetWarnAndServiceByCondition, serviceMap, new OkHttpUtil.DataCallBack() {
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            public void requestSuccess(String result) throws Exception {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                wranAndServiceModels = new ArrayList<WranAndServiceModel>();
                for (int i = 0; i < dataArray.length(); i++) {
                    WranAndServiceModel weekWeatherModel = new WranAndServiceModel(dataArray.optJSONObject(i));
                    wranAndServiceModels.add(weekWeatherModel);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                CnArticalView5 cnArticalView5 = new CnArticalView5(getActivity());
                cnArticalView5.setLayoutParams(lp);
                cnArticalView5.setData(wranAndServiceModels);
                articalLayout.addView(cnArticalView5);
            }
        });
    }

    private void getRealTimeWeek() {
        OkHttpUtil.postAsync(OkHttpUtil.GetRealTimeForecast, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                JSONObject modelObject = jsonArray.optJSONObject(0);
                RealTimeWeatherModel realTimeWeatherModel = new RealTimeWeatherModel(modelObject);
                dateTime.setText(modelObject.optString("ForecastTime"));
                int isDay = modelObject.optInt("IsDay");
                if(isDay == 0) {
                    gifView.setGifResource(getImageResourceId("g" + modelObject.optString("WeatherPhenomenonID")));
                }else{
                    gifView.setGifResource(getImageResourceId("wg" + modelObject.optString("WeatherPhenomenonID")));
                }
                ViewTreeObserver viewTreeObserver = gifView.getViewTreeObserver();
                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        int width = gifView.getMeasuredWidth();
                        int height = gifView.getMeasuredHeight();
                        if(width!=0) {
                            gifView.getViewTreeObserver().removeOnPreDrawListener(this);
                            WindowManager wm = (WindowManager) getContext()
                                    .getSystemService(Context.WINDOW_SERVICE);

                            int width1 = wm.getDefaultDisplay().getWidth();
                            int height1 = wm.getDefaultDisplay().getHeight();
//                            Log.e("width",""+gifView.getMeasuredWidth());
//                            Log.e("width1",""+(float)width1/(float)width+2);

                            gifView.setScaleX((((float)width1/(float)width)+1)*(float)1.1);
                            gifView.setScaleY(((float)height1/(float)height+1)*(float)1.1);
                        }
                        return false;
                    }
                });
                setRealTimeDatas(realTimeWeatherModel);
            }
        });
    }

    private void setRealTimeDatas(final RealTimeWeatherModel realTimeWeatherModel) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                direction.setText(realTimeWeatherModel.getWIColumn1()+" "+ realTimeWeatherModel.getWIColumn2());
                if (realTimeWeatherModel.getFireWarning().equals("")) {
                    line_weather_fire.setVisibility(View.GONE);
                } else {
                    line_weather_fire.setVisibility(View.VISIBLE);
                    fire.setText("防火指数：" + realTimeWeatherModel.getFireWarning());
                }
                wet.setText("空气湿度：" + realTimeWeatherModel.getTHColumn10());
                temp.setText(realTimeWeatherModel.getCurrentTemperature() + "℃");
                weather.setText(realTimeWeatherModel.getWeatherPhenomenon());
                if (realTimeWeatherModel.getWarningAndServiceID()!=-1){
                    mRelaWarning.setVisibility(View.VISIBLE);
                    getWarnList(realTimeWeatherModel.getWarningAndServiceID());
                }else {
                    mPBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getWarnList(int id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(OkHttpUtil.AGetWarnList + "?paramWarningAndServiceID="+id).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "网络延迟", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    final List<WarnListInfoModel> warnListInfoModels = new ArrayList<WarnListInfoModel>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        WarnListInfoModel info = new WarnListInfoModel(object1);
                        warnListInfoModels.add(info);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setWarnListInfo(warnListInfoModels);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setWarnListInfo(List<WarnListInfoModel> warnListInfoModels) {
        for (int i = 0; i < warnListInfoModels.size(); i++) {
            titile.setText(warnListInfoModels.get(i).getWarningTitleCHinese());
            content.setText("         " + warnListInfoModels.get(i).getContentCHinese());
            date.setText("\n" + warnListInfoModels.get(i).getDepartment() + "    " + warnListInfoModels.get(i).getAddTimeCHinese());
            String bitmapNameEdur = "wr" + warnListInfoModels.get(i).getImgNumber() + "";
            Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), getImageResourceId(bitmapNameEdur));
            imageView_weather1.setImageBitmap(bitmap);
        }
        mPBar.setVisibility(View.GONE);
    }

    public int getImageResourceId(String name) {
        R.drawable drawables = new R.drawable();
        //默认的id
        int resId = R.drawable.w0;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field = R.drawable.class.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }
    public int Dp2Px( float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gifView = null;
        System.gc();
    }
    private void saveArea(String areaNO,String areaName){

        SharedPreferences sp = getActivity().getSharedPreferences("area1", Context.MODE_PRIVATE);
        sp.edit().putString("AreaNO", areaNO).putString("areaName", areaName).commit();
    }

    private void getArea(){
        SharedPreferences sp = getActivity().getSharedPreferences("area1", Context.MODE_PRIVATE);
        String areaName = sp.getString("areaName", "别力古台镇");
        String AreaNO = sp.getString("AreaNO", "53192");
        map.put("AreaNO",AreaNO);
        address.setText(areaName);
    }
    private void getSecoundAddress(String areaID){
        map.clear();
        map.put("areaid",areaID);
        OkHttpUtil.postAsync(OkHttpUtil.AGetAllAreaTwo, map, new OkHttpUtil.DataCallBack() {
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            public void requestSuccess(String result) throws Exception {
                List<AreaModel> areaModels = new ArrayList<AreaModel>();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                // popupwindow
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.view_area_popup, null);
                LinearLayout popupLayout = (LinearLayout) popupView.findViewById(R.id.popupLayout);
                for (int i = 0; i < dataArray.length(); i++) {
                    final AreaModel areaModel = new AreaModel(dataArray.optJSONObject(i));
                    areaModels.add(areaModel);
                    MnTextView mnTextView = new MnTextView(getActivity());
                    if (i == 0) {
                        mnTextView.setText(" ☆ " + areaModel.getCHineseAreaName());
                    } else {
                        mnTextView.setText("\n ☆ " + areaModel.getCHineseAreaName());
                    }
                    mnTextView.setTextColor(Color.BLUE);
                    mnTextView.setTextSize(Dp2Px(5));
                    mnTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            map.clear();
                            map.put("AreaNO", areaModel.getAreaNO());
                            map.put("type", "cn_word");
                            address.setText(areaModel.getCHineseAreaName());
                            saveArea(areaModel.getAreaNO(),areaModel.getCHineseAreaName());
                            getWeekDatas();
                            if (mPopupWindow1 != null && mPopupWindow1.isShowing()) {
                                mPopupWindow1.dismiss();
                            }
                        }
                    });
                    popupLayout.addView(mnTextView);
                }
                mPopupWindow1 = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mPopupWindow1.setTouchable(true);
                mPopupWindow1.setOutsideTouchable(true);
                mPopupWindow1.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
                mPopupWindow1.showAsDropDown(btnLocation);
            }
        });
    }
    private void updateWeather(TextArticleTitle textArticleTitle){
        map.clear();
        map.put("AreaNO", textArticleTitle.getAreaOn());
        map.put("type", "cn_word");
        address.setText(textArticleTitle.getTitle());
        saveArea(textArticleTitle.getAreaOn(),textArticleTitle.getTitle());
        getRealTimeWeek();
        getWeekDatas();
    }
    @Override
    public void onAddressChange(int tag,final TextArticleTitle tat) {
        if(tag == 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateWeather(tat);
                }
            });
        }
    }
}
