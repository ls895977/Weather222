package com.naran.route;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.naran.model.ProvinceAreaModel;
import com.naran.weather.R;

import java.util.List;

/**
 * Created by darhandarhad on 2017/11/9.
 */

public class ProvinceAreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProvinceAreaModel> provinceAreaModels;
    private Context context;
    static OnFooterClickListner onFooterClickListner;
    private static TextView loadText;
    private static ProgressBar progressBar;
    private FooterView footerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
        }
    }

    public static class FooterView extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public FooterView(View v) {
            super(v);
            loadText = (TextView) v
                    .findViewById(R.id.loadmore);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            loadText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (onFooterClickListner != null) {
                        onFooterClickListner.OnFooterClickListner();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    loadText.setText("\uE266\uE317\uE27E\uE320\uE26C\uE2FA\uE26C\uE31D\uE28D \uE2C1\uE26D\uE281\uE2B6\uE26A ");
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProvinceAreaAdapter(List<ProvinceAreaModel> provinceAreaModels, Context context) {
        this.provinceAreaModels = provinceAreaModels;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view

        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_privince, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        } else {
            if (footerView == null) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_view_loadmore, null);
                footerView = new FooterView(view);
            }
            return footerView;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 final int position) {
        if (position != this.getItemCount() - 1) {

            ((ViewHolder) holder).mTextView
                    .setText(provinceAreaModels.get(position).getCnAreaName());
            ((ViewHolder) holder).mTextView
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            RouteActivity routeActivity = (RouteActivity) context;
                            routeActivity.setProviceName(provinceAreaModels.get(position).getCnAreaName());
                        }
                    });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return provinceAreaModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position != this.getItemCount() - 1)
            return 0;
        else
            return 1;
    }

    public void setOnFooterClickListner(
            OnFooterClickListner onFooterClickListner) {
        this.onFooterClickListner = onFooterClickListner;
    }

    public interface OnFooterClickListner {
        public void OnFooterClickListner();
    }

    public void Loadfinish() {

        // if (progressBar != null && loadText != null) {
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void Loadend() {

        progressBar.setVisibility(View.GONE);
        loadText.setVisibility(View.GONE);
    }

    public void Loading() {
        if(null!=progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if(null!=loadText)
        loadText.setVisibility(View.VISIBLE);
    }
}
