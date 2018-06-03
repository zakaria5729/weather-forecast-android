package com.android.zakaria.weatherappdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zakaria.weatherappdemo.R;

import java.util.ArrayList;

public class HourlyHorizontalAdapter extends RecyclerView.Adapter<HourlyHorizontalAdapter.HourlyHorizontalViewHolder> {

    private Context context;
    private ArrayList<String> hourlyTimeList;
    private ArrayList<String> hourlyPercentList;
    private ArrayList<String> hourlyTempList;
    private ArrayList<String> hourlyIconList;

    public HourlyHorizontalAdapter(Context context, ArrayList<String> hourlyTimeList, ArrayList<String> hourlyPercentList, ArrayList<String> hourlyTempList, ArrayList<String> hourlyImgList) {
        this.context = context;
        this.hourlyTimeList = hourlyTimeList;
        this.hourlyPercentList = hourlyPercentList;
        this.hourlyTempList = hourlyTempList;
        this.hourlyIconList = hourlyImgList;
    }

    @NonNull
    @Override
    public HourlyHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_hourly_layout, parent, false);
        return new HourlyHorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyHorizontalViewHolder holder, int position) {
        holder.hourlyTimeTV.setText(hourlyTimeList.get(position));
        holder.hourlyPercentTV.setText(hourlyPercentList.get(position));
        holder.hourlyTempTV.setText(hourlyTempList.get(position));
        holder.hourlyIconView.setImageResource(R.drawable.hourly);
    }

    @Override
    public int getItemCount() {
        return hourlyTimeList.size();
    }

    public class HourlyHorizontalViewHolder extends RecyclerView.ViewHolder {
        TextView hourlyTimeTV, hourlyPercentTV, hourlyTempTV;
        ImageView hourlyIconView;

        public HourlyHorizontalViewHolder(View itemView) {
            super(itemView);
            hourlyTimeTV = itemView.findViewById(R.id.hourlyTimeTV);
            hourlyPercentTV = itemView.findViewById(R.id.hourlyPercentTV);
            hourlyTempTV = itemView.findViewById(R.id.hourlyTempTV);
            hourlyIconView = itemView.findViewById(R.id.hourlyIconTV);
        }
    }
}