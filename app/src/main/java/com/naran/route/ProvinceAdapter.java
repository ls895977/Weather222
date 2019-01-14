package com.naran.route;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naran.model.ProvinceModel;
import com.naran.weather.R;

import java.util.List;

/**
 * Created by darhandarhad on 2017/11/9.
 */

public class ProvinceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProvinceModel> provinceModels;
    private Context context;
    static OnFooterClickListner onFooterClickListner;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ProvinceAdapter(List<ProvinceModel> provinceModels, Context context) {
        this.provinceModels = provinceModels;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_privince, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 final int position) {

        ((ViewHolder) holder).mTextView
                .setText(provinceModels.get(position).getProvinceName());
        ((ViewHolder) holder).mTextView
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        RouteActivity routeActivity = (RouteActivity) context;
                        routeActivity.setProviceAreaID(provinceModels.get(position).getProvinceID(),provinceModels.get(position).getProvinceName());
                    }
                });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return provinceModels.size() ;
    }

    public void setOnFooterClickListner(
            OnFooterClickListner onFooterClickListner) {
        this.onFooterClickListner = onFooterClickListner;
    }

    public interface OnFooterClickListner {
        public void OnFooterClickListner();
    }
}
