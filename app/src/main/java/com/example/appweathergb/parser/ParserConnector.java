package com.example.appweathergb.parser;

import android.content.Context;

import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.network.model.WeatherRequest;

public interface ParserConnector {
    void parse(WeatherRequest weatherRequest, Context context, String exception);

    interface BackParser {
        void dataWeather(WeatherView weatherView, String exception);
    }
}
