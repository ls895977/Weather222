package com.naran.useraccount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.model.UserInfoModel;
import com.naran.weather.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


public class LoginActivity extends Activity {

    private Button login;
    private EditText etUserName;
    private EditText etPassWord;
    private   UserInfoModel userInfoModel;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //80 8001   pinglun sousuo ,cms,    www. comment .so
        initViews();
        getLoginInfo();
    }
    private void initViews(){
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        register = (Button)findViewById(R.id.regist);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        login = (Button)findViewById(R.id.login);
        etUserName = (EditText)findViewById(R.id.phoneNumber);
        etPassWord = (EditText)findViewById(R.id.passWord);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map = new HashMap<String, String>();
                map.put("userName",etUserName.getText().toString().trim());
                map.put("passWord",etPassWord.getText().toString().trim());
                OkHttpUtil.postAsync(OkHttpUtil.Login, map, new OkHttpUtil.DataCallBack() {
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(LoginActivity.this,  "连接失败!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void requestSuccess(String result) throws Exception {

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        userInfoModel = new UserInfoModel(jsonArray.optJSONObject(0));
                        Log.e("userInfoModel","userInfoModel");
                        LoginUtil.getInstance().userInfoModel = userInfoModel;
                        LoginUtil.getInstance().isLogined=true;
                        saveLoginInfo();
                        finish();
                    }
                });
            }
        });
    }
    private void getLoginInfo(){
        SharedPreferences sp = getSharedPreferences("loginInfo",
                Activity.MODE_PRIVATE);
        String name = sp.getString("UserName","");
        String passWord = sp.getString("PassWord","");
        etUserName.setText(name);
        etPassWord.setText(passWord);
    }
    private void saveLoginInfo() {

        SharedPreferences sp = getSharedPreferences("loginInfo",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserName", etUserName.getText().toString().trim());
        editor.putString("PassWord", etPassWord.getText().toString().trim());
        editor.commit();
    }
}
