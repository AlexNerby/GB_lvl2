package com.example.appweathergb.observers;

import com.example.appweathergb.network.model.WeatherRequest;

public interface WeatherConnector {
    void update(WeatherRequest weatherRequest, String exception);
}
