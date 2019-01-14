package com.naran.useraccount;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

import static com.naran.Utils.LoginUtil.userInfoModel;

public class RegisterActivity extends Activity {

    private EditText etUserName;
    private EditText etPassWord;
    private Button regist;
    private RelativeLayout progressLayout;
    private LinearLayout checkBoxLayout;
    private String selectedItem = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }
    private void initViews(){
        checkBoxLayout = (LinearLayout)findViewById(R.id.checkBoxLayout);

        etUserName = (EditText)findViewById(R.id.phoneNumber);
        etPassWord = (EditText)findViewById(R.id.passWord);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressLayout = (RelativeLayout)findViewById(R.id.progressLayout);
        regist = (Button)findViewById(R.id.regist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMobileNO(etUserName.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, "手机号码格式错误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressLayout.setVisibility(View.VISIBLE);
                Map<String,String> map = new HashMap<String, String>();
                map.put("userName",etUserName.getText().toString().trim());
                map.put("passWord",etPassWord.getText().toString().trim());
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
                OkHttpUtil.postAsync(OkHttpUtil.register, map, new OkHttpUtil.DataCallBack() {
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        Toast.makeText(RegisterActivity.this,  "连接失败!", Toast.LENGTH_SHORT).show();
                        progressLayout.setVisibility(View.GONE);
                    }
                    @Override
                    public void requestSuccess(String result) throws Exception {

                        progressLayout.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject.optString("result").equals("")){
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            goLogin();
                        }else{
                            Toast.makeText(RegisterActivity.this, jsonObject.optString("result"), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    public static boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);



        return m.matches();

    }
    private void goLogin(){
        Map<String,String> map = new HashMap<String, String>();
        map.put("userName",etUserName.getText().toString().trim());
        map.put("passWord",etPassWord.getText().toString().trim());
        OkHttpUtil.postAsync(OkHttpUtil.Login, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(RegisterActivity.this,  "连接失败!", Toast.LENGTH_SHORT).show();
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
    private void saveLoginInfo() {

        SharedPreferences sp = getSharedPreferences("loginInfo",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserName", etUserName.getText().toString().trim());
        editor.putString("PassWord", etPassWord.getText().toString().trim());
        editor.commit();

    }
}

