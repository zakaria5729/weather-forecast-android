package com.android.zakaria.weatherappdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zakaria.weatherappdemo.R;
import com.android.zakaria.weatherappdemo.models.RetrofitClient;
import com.android.zakaria.weatherappdemo.models.WeatherApi;
import com.android.zakaria.weatherappdemo.pojos.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyDetailsActivity extends AppCompatActivity {

    private int id;
    private String customUrl;
    private String summaryStr;
    private WeatherApi weatherApi;
    private static final String URL = "forecast/44af292051fdb50297c330b7e270abc9/";

    private TextView summary, dewPoint, pressure, windSpeed, windGust, windBearing, cloudCover, ozone, weekName, percent, highTemp, lowTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_details);

        weekName = findViewById(R.id.weekName);
        percent = findViewById(R.id.humidityPercent);
        highTemp = findViewById(R.id.highTempTV);
        lowTemp = findViewById(R.id.lowTempTV);
        summary = findViewById(R.id.summaryTV);
        dewPoint = findViewById(R.id.dewPointTV);
        pressure = findViewById(R.id.pressureTV);
        windSpeed = findViewById(R.id.windSpeedTV);
        windGust = findViewById(R.id.windGustTV);
        windBearing = findViewById(R.id.windBearingTV);
        cloudCover = findViewById(R.id.cloudCoverTV);
        ozone = findViewById(R.id.ozoneTV);

        Intent intent = getIntent();
        id = intent.getIntExtra("weekly_id", -1);
        String nameStr = intent.getStringExtra("weekly_name");
        String percentStr = intent.getStringExtra("weekly_percent");
        String highTempStr = intent.getStringExtra("weekly_high_temp");
        String lowTempStr = intent.getStringExtra("weekly_low_temp");

        weatherApi = RetrofitClient.getRetrofitClient().create(WeatherApi.class);
        customUrl = URL + MainActivity.getSelectedArea();

        Call<Weather> callWeatherCondition = weatherApi.getWeather(customUrl);
        callWeatherCondition.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather weather = response.body();

                dewPoint.setText("dewPoint : " + weather.getDaily().getData().get(id).getDewPoint());
                pressure.setText("pressure : " + weather.getDaily().getData().get(id).getPressure());
                windSpeed.setText("windSpeed : " + weather.getDaily().getData().get(id).getWindSpeed());
                windGust.setText("windGust : " + weather.getDaily().getData().get(id).getWindGust());
                windBearing.setText("windBearing : " + weather.getDaily().getData().get(id).getWindBearing());
                cloudCover.setText("cloudCover : " + weather.getDaily().getData().get(id).getCloudCover());
                ozone.setText("ozone : " + weather.getDaily().getData().get(id).getOzone());
                summary.setText("Summary : " + weather.getDaily().getData().get(id).getSummary());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(WeeklyDetailsActivity.this, "Internet connection needed", Toast.LENGTH_SHORT).show();
            }
        });

        weekName.setText("Day name : " + nameStr);
        percent.setText("Humidity : " + percentStr);
        highTemp.setText("High Temperature : " + highTempStr);
        lowTemp.setText("Low Temperature : " + lowTempStr);
    }
}
