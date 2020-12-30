package com.example.appweathergb.repository;

import com.example.appweathergb.network.model.WeatherRequest;

public interface OutputInteractor {

    void returnWeatherRequest(WeatherRequest weatherRequest);

}
