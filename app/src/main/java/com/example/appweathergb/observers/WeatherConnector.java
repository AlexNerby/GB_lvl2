package com.example.appweathergb.observers;

import com.example.appweathergb.model.WeatherRequest;

public interface WeatherConnector {
    void update(WeatherRequest weatherRequest, String exception);
}
