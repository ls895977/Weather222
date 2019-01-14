package com.naran.favorite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Request;

public class FavoriteActivity extends Activity {

    private Button post;
    private LinearLayout checkBoxLayout;
    private RelativeLayout progressLayout;
    private String selectedItem = "";
    private CheckBox honi, uher, aduu, temee, erdenixixi, boodai, borqag, tumus, oixigui;
    private List<String> ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initViews();
    }
    
    private void initViews() {

        honi = (CheckBox) findViewById(R.id.imaa);
        uher = (CheckBox) findViewById(R.id.uher);
        aduu = (CheckBox) findViewById(R.id.aduu);
        temee = (CheckBox) findViewById(R.id.temee);
        erdenixixi = (CheckBox) findViewById(R.id.erdeninxixi);
        boodai = (CheckBox) findViewById(R.id.boodai);
        borqag = (CheckBox) findViewById(R.id.borqag);
        tumus = (CheckBox) findViewById(R.id.tumus);
        oixigui = (CheckBox) findViewById(R.id.xigui);

        honi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("1");
                }else{
                    ids.remove("1");
                }
            }
        });
        uher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("2");
                }else{
                    ids.remove("2");
                }
            }
        });
        aduu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("3");
                }else{
                    ids.remove("3");
                }
            }
        });
        temee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("4");
                }
                else{
                    ids.remove("4");
                }
            }
        });
        erdenixixi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("5");
                }else{
                    ids.remove("5");
                }
            }
        });
        boodai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("6");
                }else{
                    ids.remove("6");
                }
            }
        });
        borqag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("7");
                }else{
                    ids.remove("7");
                }
            }
        });
        tumus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("8");
                }else{
                    ids.remove("8");
                }
            }
        });
        oixigui.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ids.add("9");
                }
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkBoxLayout = (LinearLayout) findViewById(R.id.checkBoxLayout);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressLayout.setVisibility(View.VISIBLE);
                Map<String, String> map = new HashMap<String, String>();

                for (int i = 0; i < ids.size(); i++) {
                    if(i==0){
                        selectedItem = ids.get(i);
                    }else{
                        selectedItem = selectedItem+","+ids.get(i);
                    }
                }
                map.put("InterestIds", selectedItem);
                if (LoginUtil.getInstance().isLogined && null != LoginUtil.userInfoModel) {
                    map.put("userid", LoginUtil.userInfoModel.getID() + "");
                    OkHttpUtil.postAsync(OkHttpUtil.AUpdateInterestIds, map, new OkHttpUtil.DataCallBack() {
                        @Override
                        public void requestFailure(Request request, IOException e) {
                            Toast.makeText(FavoriteActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void requestSuccess(String result) throws Exception {
                            Toast.makeText(FavoriteActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("userid", LoginUtil.userInfoModel.getID() + "");
        OkHttpUtil.postAsync(OkHttpUtil.AGetInterestIds, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(FavoriteActivity.this, "获取失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws ExecutionException {
                Log.e("result", result);
                //{"InterestIDs":"1,4"}
                progressLayout.setVisibility(View.GONE);
                if (null != result) {
                    try {
                        JSONObject jb = new JSONObject(result);
                        String ids = jb.optString("InterestIDs");
                        String[] mids = ids.split(",");
                        for (int i = 0; i < mids.length; i++) {
                            int temp = Integer.parseInt(mids[i]);
                            Log.e("temp","temp = "+temp);
                            switch (temp) {
                                case 1:
                                    honi.setChecked(true);
                                    break;
                                case 2:
                                    uher.setChecked(true);
                                    break;
                                case 3:
                                    aduu.setChecked(true);
                                    break;
                                case 4:
                                    temee.setChecked(true);
                                    break;
                                case 5:
                                    erdenixixi.setChecked(true);
                                    break;
                                case 6:
                                    boodai.setChecked(true);
                                    break;
                                case 7:
                                    borqag.setChecked(true);
                                    break;
                                case 8:
                                    tumus.setChecked(true);
                                    break;
                                case 9:
                                    oixigui.setChecked(true);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
