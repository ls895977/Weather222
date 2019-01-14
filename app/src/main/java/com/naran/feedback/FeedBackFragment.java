package com.naran.feedback;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.controls.NaranButton;
import com.naran.useraccount.LoginActivity;
import com.naran.weather.MainActivity;
import com.naran.weather.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


public class FeedBackFragment extends Fragment {

    private int userID = 22;
    private int areaNo = 53192;
    private NaranButton btnSend;
    private View view;
    private Map<String, String> map = new HashMap<>();
    private EditText editText;
    private RelativeLayout msgLayout, sendLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed_back, container, false);
        initViews();
        if(getActivity() instanceof MainActivity){
            btnSend.setBackgroundResource(R.drawable.post);
        }else{

            btnSend.setBackgroundResource(R.drawable.posthitad);
        }
        return view;
    }
    private void initViews() {
        msgLayout = (RelativeLayout) view.findViewById(R.id.msgLayout);
        sendLayout = (RelativeLayout) view.findViewById(R.id.sendLayout);
        editText = (EditText) view.findViewById(R.id.editText);
        btnSend = (NaranButton) view.findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "请输入内容！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!LoginUtil.getInstance().isLogined){
                    startActivity(new Intent().setClass(getActivity(), LoginActivity.class));
                }else {
                    sendLayout.setVisibility(View.VISIBLE);
                    map.put("addUserId", LoginUtil.userInfoModel.getID()+"");
                    map.put("areaNo", LoginUtil.userInfoModel.getAreaNO());
                    map.put("content", editText.getText().toString());
                    OkHttpUtil.postAsync(OkHttpUtil.AddFeedBack, map, new OkHttpUtil.DataCallBack() {
                        @Override
                        public void requestFailure(Request request, IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            sendLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void requestSuccess(String result) throws Exception {
                            editText.setText("");
                            Toast.makeText(getActivity(), "反馈成功！", Toast.LENGTH_SHORT).show();
                            sendLayout.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

}
