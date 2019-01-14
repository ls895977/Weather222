package com.naran.addressmanager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naran.addresmanager1.FirstAddress1Adapter;
import com.naran.controls.MnTextView;
import com.naran.weather.R;

import java.util.List;

/**
 * Created by darhandarhad on 2018/12/12.
 */

public class FirstAddressAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TextArticleTitle> textArticleTitles;
    private Context context;
    private int tag;
    private int selectedItemIndex = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View bg;
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            bg = v.findViewById(R.id.bg);
            mTextView = (TextView) v.findViewById(R.id.title);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public FirstAddressAdapter(List<TextArticleTitle> titleAndUrls, Context context, int tag) {
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
                R.layout.item_view_address_first, parent, false);
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
            ((ViewHolder) holder).mTextView
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            selectedItemIndex = position;
                            notifyDataSetChanged();
                            OnAddressClickTask.getInstance().fireMsg(tag, textArticleTitles.get(position));
                        }
                    });
            if(position == selectedItemIndex) {
                ((ViewHolder) holder).bg.setBackgroundColor(Color.WHITE);
            } else {
                ((ViewHolder) holder).bg.setBackgroundColor(Color.parseColor("#eeeeee"));
            }

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
}