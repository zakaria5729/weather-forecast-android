package com.android.zakaria.weatherappdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zakaria.weatherappdemo.R;
import com.android.zakaria.weatherappdemo.activities.WeeklyDetailsActivity;

import java.util.ArrayList;

public class WeeklyHorizontalAdapter  extends RecyclerView.Adapter<WeeklyHorizontalAdapter.WeeklyHorizontalViewHolder> {

    private Context context;
    private ArrayList<String> weeklyDayList;
    private ArrayList<String> weeklyPercentList;
    private ArrayList<String> weeklyHighTempList;
    private ArrayList<String> weeklyLowTempList;
    private ArrayList<String> weeklyIconList;
    private int pos;


    public WeeklyHorizontalAdapter(Context context, ArrayList<String> weeklyTimeList, ArrayList<String> weeklyPercentList, ArrayList<String> weeklyTempList, ArrayList<String> weeklyLowTempList, ArrayList<String> weeklyImgList) {
        this.context = context;
        this.weeklyDayList = weeklyTimeList;
        this.weeklyPercentList = weeklyPercentList;
        this.weeklyHighTempList = weeklyTempList;
        this.weeklyLowTempList = weeklyLowTempList;
        this.weeklyIconList = weeklyImgList;
    }


    @NonNull
    @Override
    public WeeklyHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_weekly_layout, parent, false);
        return new WeeklyHorizontalViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WeeklyHorizontalViewHolder holder, final int position) {
        holder.weeklyDayTV.setText(weeklyDayList.get(position));
        holder.weeklyPercentTV.setText(weeklyPercentList.get(position));
        holder.weeklyHighTempTV.setText(weeklyHighTempList.get(position));
        holder.weeklyLowTempTV.setText(weeklyLowTempList.get(position));
        holder.weekIconView.setImageResource(R.drawable.weekly);


        holder.weeklyDayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                Intent intent = new Intent(context,WeeklyDetailsActivity.class);
                intent.putExtra("weekly_id", pos);
                intent.putExtra("weekly_name", weeklyDayList.get(position));
                intent.putExtra("weekly_percent", weeklyPercentList.get(position));
                intent.putExtra("weekly_high_temp", weeklyHighTempList.get(position));
                intent.putExtra("weekly_low_temp", weeklyLowTempList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weeklyDayList.size();
    }

    public class WeeklyHorizontalViewHolder extends RecyclerView.ViewHolder {
        TextView weeklyDayTV, weeklyPercentTV, weeklyHighTempTV, weeklyLowTempTV;
        ImageView weekIconView;

        public WeeklyHorizontalViewHolder(View itemView) {
            super(itemView);
            weeklyDayTV = itemView.findViewById(R.id.weekDayTV);
            weeklyPercentTV = itemView.findViewById(R.id.weekPercent);
            weeklyHighTempTV = itemView.findViewById(R.id.weekTempHigh);
            weeklyLowTempTV = itemView.findViewById(R.id.weekTempLow);
            weekIconView = itemView.findViewById(R.id.weekImg);

            weeklyDayTV.setPaintFlags(weeklyDayTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
