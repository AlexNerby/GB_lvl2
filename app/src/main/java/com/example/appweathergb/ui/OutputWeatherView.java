package com.example.appweathergb.ui;

import com.example.appweathergb.entities.WeatherView;

import java.util.List;

public interface OutputWeatherView {

    void WeatherViewRequest(String city, String temp, String speed);


    //:TODO отправлять в SearchActivity
    void allWeather(List<WeatherView> weatherViewList);
}
