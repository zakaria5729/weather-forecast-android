package com.android.zakaria.weatherappdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zakaria.weatherappdemo.R;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {

    private Context context;
    private int[] currentMoreIconArray;
    private ArrayList<String> currentMoreArrayList;
    private String[] currentlyMoreTitleArray;


    public MyListViewAdapter(@NonNull Context context, int[] currentMoreIconArray, String[] currentlyMoreTitleArray, ArrayList<String> currentMoreArrayList) {
        //super(context, R.layout.activity_custom_current_layout);
        this.context = context;
        this.currentMoreArrayList = currentMoreArrayList;
        this.currentMoreIconArray = currentMoreIconArray;
        this.currentlyMoreTitleArray = currentlyMoreTitleArray;
    }

    @Override
    public int getCount() {
        return currentMoreArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.activity_custom_current_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder();
        if (convertView == null) {
            myViewHolder.titleTextView = convertView.findViewById(R.id.currentMoreTextId);
            myViewHolder.imageView = convertView.findViewById(R.id.currentImgIcon);
            myViewHolder.dataTextView = convertView.findViewById(R.id.currentMoreDataId);

            convertView.setTag(myViewHolder);
        }
        else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.dataTextView.setText(currentMoreArrayList.get(position));
       /* try {


        }
        catch (Exception e) {
            Log.d("my tag ", e.getMessage());
        }*/

        myViewHolder.imageView.setImageResource(currentMoreIconArray[position]);
        myViewHolder.titleTextView.setText(currentlyMoreTitleArray[position]);
        return convertView;
    }

    static class MyViewHolder {
        TextView titleTextView, dataTextView;
        ImageView imageView;
    }
}