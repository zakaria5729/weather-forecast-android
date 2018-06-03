package com.android.zakaria.weatherappdemo.models;

import com.android.zakaria.weatherappdemo.pojos.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherApi {
    @GET
    Call<Weather> getWeather(@Url String url);
}
