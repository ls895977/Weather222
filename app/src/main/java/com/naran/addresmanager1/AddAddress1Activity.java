package com.naran.addresmanager1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.addressmanager.OnAddressClickListener;
import com.naran.addressmanager.OnAddressClickTask;
import com.naran.addressmanager.TextArticleTitle;
import com.naran.connector.OkHttpUtil;
import com.naran.weather.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class AddAddress1Activity extends Activity implements OnAddressClickListener {

    private RecyclerView firstRecyclerView;
    private RecyclerView secoundRecyclerView;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager layoutManager2;

    private FirstAddress1Adapter firstAdapter;
    private SecondAddress1Adapter secoundAdapter;
    private List<TextArticleTitle> firstTextArticleTitles = new ArrayList<>();
    private List<TextArticleTitle> secoundTextArticleTitles = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private ProgressDialog dialog;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address1);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initViews();
        OnAddressClickTask.getInstance().addListener(this);
        getFirstData();
    }

    private void initViews() {

        firstRecyclerView = (RecyclerView) findViewById(R.id.firstRecyclerView);
        secoundRecyclerView = (RecyclerView) findViewById(R.id.secoundRecyclerView);

        firstAdapter = new FirstAddress1Adapter(firstTextArticleTitles, AddAddress1Activity.this,1);
        secoundAdapter = new SecondAddress1Adapter(secoundTextArticleTitles, AddAddress1Activity.this,2);
////////11111111111111
        firstRecyclerView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager1)
                .setOrientation(LinearLayoutManager.VERTICAL);
        firstRecyclerView.setLayoutManager(layoutManager1);

        firstRecyclerView.setAdapter(firstAdapter);
        //////22222222222222
        secoundRecyclerView.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager2)
                .setOrientation(LinearLayoutManager.VERTICAL);
        secoundRecyclerView.setLayoutManager(layoutManager2);

        secoundRecyclerView.setAdapter(secoundAdapter);

        dialog = new ProgressDialog(this);
    }

    private void getFirstData() {

        OkHttpUtil.getAsync(OkHttpUtil.AGetAllAreaOne, new OkHttpUtil.DataCallBack() {
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(AddAddress1Activity.this, "获取地区数据失败！", Toast.LENGTH_SHORT).show();
            }

            public void requestSuccess(String result) throws Exception {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                // popupwindow
                for (int i = 0; i < dataArray.length(); i++) {
                    TextArticleTitle tat = new TextArticleTitle();
                    tat.setContent(dataArray.optJSONObject(i).optString("AreaNO"));
                    tat.setItemID(dataArray.optJSONObject(i).optInt("AreaID"));
                    String cn = dataArray.optJSONObject(i).optString("CHineseAreaName");
                    String sb = "";
                    for(int j=0;j<cn.length();j++){
                        sb += cn.charAt(j);
                        if(j<cn.length()-1){
                            sb += "\n";
                        }
                    }
                    tat.setTitle(sb);
                    firstTextArticleTitles.add(tat);
                }
                getSecondData(firstTextArticleTitles.get(0).getItemID());
                firstAdapter.notifyDataSetChanged();
            }
        });
    }
    private void getSecondData(int id) {

        map.put("areaid", id+"");
        OkHttpUtil.postAsync(OkHttpUtil.AGetAllAreaTwo, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(AddAddress1Activity.this, "获取地区数据失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.optJSONArray("data");
                // popupwindow
                secoundTextArticleTitles.clear();
                for (int i = 0; i < dataArray.length(); i++) {
                    TextArticleTitle tat = new TextArticleTitle();
                    tat.setContent(dataArray.optJSONObject(i).optString("AreaNO"));
                    tat.setItemID(dataArray.optJSONObject(i).optInt("AreaID"));
                    tat.setTitle(dataArray.optJSONObject(i).optString("CHineseAreaName"));
                    tat.setCurrentTemperature(dataArray.optJSONObject(i).optString("CurrentTemperature"));
                    tat.setWeatherPhenomenonID(dataArray.optJSONObject(i).optInt("WeatherPhenomenonID"));
                    tat.setWeatherPhenomenon(dataArray.optJSONObject(i).optString("WeatherPhenomenonCn"));
                    secoundTextArticleTitles.add(tat);
                }
                secoundAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

    }

    private void aInsertMyArea(int id) {

        map.put("areaid", id+"");
        map.put("userid", LoginUtil.userInfoModel.getID()+"");
        OkHttpUtil.postAsync(OkHttpUtil.AInsertMyArea, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(AddAddress1Activity.this, "提交失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                Toast.makeText(AddAddressActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public void onClick(int tag, TextArticleTitle textArticleTitle) {
        if(tag == 1) {
            dialog.show();
            getSecondData(textArticleTitle.getItemID());
//            Toast.makeText(this, "AddAddressActivity---  "+id, Toast.LENGTH_SHORT).show();
        }else if (tag == 2){
            aInsertMyArea(textArticleTitle.getItemID());
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        OnAddressClickTask.getInstance().removeListener(this);
    }
}
