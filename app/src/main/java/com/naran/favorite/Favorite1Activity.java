package com.naran.favorite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Request;

public class Favorite1Activity extends Activity {

    private Button post;
    private LinearLayout checkBoxLayout;
    private RelativeLayout progressLayout;
    private String selectedItem = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite1);
        initViews();
    }
    private void initViews(){

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkBoxLayout = (LinearLayout)findViewById(R.id.checkBoxLayout);
        progressLayout = (RelativeLayout)findViewById(R.id.progressLayout);
        post = (Button)findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressLayout.setVisibility(View.VISIBLE);
                Map<String,String> map = new HashMap<String, String>();
                for (int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout)checkBoxLayout.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        CheckBox checkBox  = (CheckBox) linearLayout.getChildAt(j);
                        if(checkBox.isChecked()){
                            int temp = i*3+j+1;
                            if(selectedItem.equals("")) {
                                selectedItem = temp+"";
                            }else{
                                selectedItem = selectedItem+","+temp;
                            }
                        }
                    }
                }
                map.put("InterestIds",selectedItem);
                if(LoginUtil.getInstance().isLogined&&null!=LoginUtil.userInfoModel){
                    map.put("userid",LoginUtil.userInfoModel.getID()+"");
                    OkHttpUtil.postAsync(OkHttpUtil.AUpdateInterestIds, map, new OkHttpUtil.DataCallBack() {
                        @Override
                        public void requestFailure(Request request, IOException e) {
                            Toast.makeText(Favorite1Activity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void requestSuccess(String result) throws Exception {
                            Toast.makeText(Favorite1Activity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
        Map<String,String> map = new HashMap<>();
        map.put("userid",LoginUtil.userInfoModel.getID()+"");
        OkHttpUtil.postAsync(OkHttpUtil.AGetInterestIds, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(Favorite1Activity.this, "获取失败！", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void requestSuccess(String result) throws ExecutionException {
                Log.e("result",result);
                //{"InterestIDs":"1,4"}
                progressLayout.setVisibility(View.GONE);
                if(null!=result){
                    try {
                        JSONObject jb = new JSONObject(result);
                        String ids = jb.optString("InterestIDs");
                        String[] mids = ids.split(",");
                        for (int i = 0; i < checkBoxLayout.getChildCount(); i++) {
                            LinearLayout linearLayout = (LinearLayout)checkBoxLayout.getChildAt(i);
                            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                                CheckBox checkBox  = (CheckBox) linearLayout.getChildAt(j);
                                for (int k = 0; k < mids.length; k++) {
                                    int temp = i*3+j+1;
                                    String tempstr = temp+"";
                                    if(tempstr.equals(mids[k])){
                                        checkBox.setChecked(true);
                                    }
                                }
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
