package com.aountech.heartdiseasesprediction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aountech.heartdiseasesprediction.R;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
    LayoutInflater inflater;
    List<String> tipsTitles;
    List<String> tipsInfos;

    public TipsAdapter(Context context, List<String> tipsTitles, List<String> tipsInfos) {
        this.inflater = LayoutInflater.from(context);
        this.tipsTitles = tipsTitles;
        this.tipsInfos = tipsInfos;
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tips_layout,parent,false);
        TipsViewHolder tipsViewHolder = new TipsViewHolder(view);
        return tipsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        TipsViewHolder viewHolder = (TipsViewHolder) holder;
        viewHolder.tv_title.setText(tipsTitles.get(position).toString());
        viewHolder.tv_info.setText(tipsInfos.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return tipsTitles.size();
    }

    class TipsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title ,tv_info;
        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tips_tv_title);
            tv_info=itemView.findViewById(R.id.tips_tv_info);
        }
    }
}
