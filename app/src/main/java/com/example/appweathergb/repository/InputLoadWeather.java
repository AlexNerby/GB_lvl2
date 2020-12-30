package com.example.appweathergb.repository;

public interface InputLoadWeather {
    void requestRetrofitByCoordinates(String lat, String lon, String key);

    void requestRetrofitByCity(String city, String key);
}
