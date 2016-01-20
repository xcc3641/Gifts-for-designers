package com.android.hugo.giftforstylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class RecycerlViewAdapter extends RecyclerView.Adapter<RecycerlViewAdapter.mViewHolder> {
    private Context context;
    private ArrayList<Integer> mixColer;

    public RecycerlViewAdapter(Context context, ArrayList<Integer> mixColer) {
        this.mixColer = mixColer;
        this.context = context;

    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new mViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));

    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.imageView.setBackgroundColor(mixColer.get(position));
    }

    @Override
    public int getItemCount() {
        return mixColer.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public mViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
