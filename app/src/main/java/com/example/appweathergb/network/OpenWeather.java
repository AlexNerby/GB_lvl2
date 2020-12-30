package com.example.appweathergb.network;

import com.example.appweathergb.network.model.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCount,
                                     @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeatherByCoordinates(@Query("lat") String lat,
                                                  @Query("lon") String lon,
                                                  @Query("appid") String keyApi);
}
