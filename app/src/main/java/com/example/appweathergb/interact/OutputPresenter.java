package com.example.appweathergb.interact;

import com.example.appweathergb.entities.WeatherView;

public interface OutputPresenter {

    void insertWeatherView(WeatherView weatherView);

    void insertGeoLocation(String country, String city);
}
