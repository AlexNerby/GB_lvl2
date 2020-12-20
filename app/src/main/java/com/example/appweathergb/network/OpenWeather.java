package com.example.appweathergb.network;

import com.example.appweathergb.network.model.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCount,
                                     @Query("appid") String keyApi);
}
