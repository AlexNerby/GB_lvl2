package com.example.appweathergb.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.interact.OutputPresenter;
import com.example.appweathergb.interact.WeatherInteractor;
import com.example.appweathergb.ui.InputPresenter;
import com.example.appweathergb.ui.OutputWeatherView;

public class WeatherPresenter implements InputPresenter, OutputPresenter {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherPresenter";

    private final InputInteractor inputInteractor;
    private final OutputWeatherView outputWeatherView;

    public WeatherPresenter(Context context) {
        if (LOG) {
            Log.v(TAG, "Constructor");
        }
        outputWeatherView = (OutputWeatherView) context;
        inputInteractor = new WeatherInteractor(this);
    }

    @Override
    public void search(String city) {
        if (LOG) {
            Log.v(TAG, "insert");
        }
        inputInteractor.searchByCity(city);
    }

    @Override
    public void searchByCoordinates() {
        inputInteractor.searchByCoordinates();
    }

    @Override
    public void insertWeatherView(WeatherView weatherView) {
        if (LOG) {
            Log.v(TAG, "insertWeatherView");
        }
        outputWeatherView.WeatherViewRequest(
                weatherView.getCity()
                , weatherView.getTemperature()
                , weatherView.getWindSpeed());
    }

    @Override
    public void insertGeoLocation(String country, String city) {

    }
}
