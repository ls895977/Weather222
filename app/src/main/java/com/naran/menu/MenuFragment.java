package com.naran.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.naran.mine.Mine1Fragment;
import com.naran.mine.MineFragment;
import com.naran.weather.R;


/**
 * Created by surleg on 2017/3/11.
 */

public class MenuFragment extends Fragment {
    private View view;
    private ImageView set;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView();
        initEvent();
        return view;
    }

    private void initEvent() {
        set.setOnClickListener(new OnClick());
    }

    private void initView() {
        set = (ImageView) view.findViewById(R.id.imageView_menu_set);
    }

    private class OnClick implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageView_menu_set:
                    Intent intent = new Intent(getActivity(), MineFragment.class);
                    startActivity(intent);
            }
        }
    }
}
