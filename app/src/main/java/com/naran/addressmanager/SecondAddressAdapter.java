package com.naran.addressmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naran.controls.GifView;
import com.naran.weather.R;

import java.util.List;

/**
 * Created by darhandarhad on 2018/12/12.
 */

public class SecondAddressAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TextArticleTitle> textArticleTitles;
    private Context context;
    private int tag;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView tvOther;
        public TextView tvTempercher;
        public GifView gifView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
            tvOther = (TextView)v.findViewById(R.id.other);
            tvTempercher = (TextView)v.findViewById(R.id.tempercher);
            gifView = (GifView)v.findViewById(R.id.gifView);
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public SecondAddressAdapter(List<TextArticleTitle> titleAndUrls, Context context, int tag) {
        this.textArticleTitles = titleAndUrls;
        this.context = context;
        this.tag = tag;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_view_address_second, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 final int position) {
        if (position != this.getItemCount()) {

            ((ViewHolder) holder).mTextView
                    .setText(textArticleTitles.get(position).getTitle());
            ((ViewHolder) holder).tvOther
                    .setText(textArticleTitles.get(position).getWeatherPhenomenon());
            ((ViewHolder) holder).tvTempercher
                    .setText(textArticleTitles.get(position).getCurrentTemperature()+"℃");
            ((ViewHolder) holder).gifView.setGifResource(getImageResourceId("g" + textArticleTitles.get(position).getWeatherPhenomenonID()));
            ((ViewHolder) holder).gifView
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                        if(context.getClass().equals(AddressManagerActivity.class)){
                            OnAddressClickTask.getInstance().fireMsg(-1,textArticleTitles.get(position));
                        }else if (context.getClass().equals(AddAddressActivity.class)){
                            OnAddressClickTask.getInstance().fireMsg(tag,textArticleTitles.get(position));
                        }
                        }
                    });

        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return textArticleTitles.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return 0;

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
}