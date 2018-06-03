package com.android.zakaria.weatherappdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.zakaria.weatherappdemo.R;
import com.android.zakaria.weatherappdemo.adapters.MyListViewAdapter;

import java.util.ArrayList;

public class CurrentMoreActivity extends AppCompatActivity {

    private int[] currentlyMoreIconArray = {R.drawable.dewpoint,
            R.drawable.pressure, R.drawable.wind_speed,
            R.drawable.windgust, R.drawable.windbearing,
            R.drawable.cloudcover, R.drawable.ozone};

    private String[] currentlyMoreTitleArray;
    private ArrayList<String> currentlyMoreArrayList;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_more);

        listView = findViewById(R.id.listViewId);

        currentlyMoreArrayList =getIntent().getStringArrayListExtra("current_more");
        currentlyMoreTitleArray = getResources().getStringArray(R.array.current_more_info_title);

        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this, currentlyMoreIconArray, currentlyMoreTitleArray, currentlyMoreArrayList);
        listView.setAdapter(myListViewAdapter);
    }
}
