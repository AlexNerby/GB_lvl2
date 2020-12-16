package com.example.appweathergb.parcer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.storage.Constants;

public class JsonParser implements ParserConnector {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherJsonParser";
    private final static int subtract = 273;

    @Override
    public void parse(WeatherRequest weatherRequest, Context context, String exception) {
        if (LOG) {
            Log.v(TAG, "метод parse + " + exception);
        }

        final ParserConnector.BackParser backParser = (ParserConnector.BackParser) context;

        final WeatherView weatherView = new WeatherView();
        weatherView.setCity(weatherRequest.getName());
        weatherView.setTemperature(convert(weatherRequest));
        weatherView.setWindSpeed(String.valueOf(weatherRequest.getWind().getSpeed()));

        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (LOG) {
                    Log.v(TAG, "runOnUiThread + " + exception);
                }
                backParser.dataWeather(weatherView, exception);
            }
        });
    }

    private String convert(WeatherRequest weatherRequest) {
        float a = weatherRequest.getMain().getTemp() - subtract;
        int result = Math.round(a);
        return String.valueOf(result) + Constants.celsius;
    }
}
