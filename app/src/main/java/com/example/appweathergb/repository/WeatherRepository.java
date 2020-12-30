package com.example.appweathergb.repository;

import android.util.Log;

import com.example.appweathergb.BuildConfig;
import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.interact.InputRepository;
import com.example.appweathergb.network.LoadWeather;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.repository.location.MyGeoLocation;

import java.util.List;

public class WeatherRepository implements InputRepository, OutputRepository {


    private static final boolean LOG = true;
    private static final String TAG = "myWeatherRepository";

    private final static String key = BuildConfig.WEATHER_API_KEY;

    private final OutputInteractor outputInteractor;
    private final InputGeoLocation inputGeoLocation;
    private final InputLoadWeather inputLoadWeather;

    public WeatherRepository(OutputInteractor outputInteractor) {
        this.outputInteractor = outputInteractor;

        inputGeoLocation = new MyGeoLocation(this);
        inputLoadWeather = new LoadWeather(this);
    }

    @Override
    public void searchByCity(String city) {
        if (LOG) {
            Log.v(TAG, "requestRetrofit");
        }
        inputLoadWeather.requestRetrofitByCity(city, key);
    }

    @Override
    public void searchByCoordinates() {
        inputGeoLocation.startGeoLocation();
    }

    @Override
    public void returnGeoLocation(String lat, String lon) {
        inputLoadWeather.requestRetrofitByCoordinates(lat, lon, key);
    }

    @Override
    public void returnNotPermission() {
        //TODO
    }

    @Override
    public void returnRequestRetrofit(WeatherRequest weatherRequest) {
        outputInteractor.returnWeatherRequest(weatherRequest);
    }

    @Override
    public void returnRequestRetrofitException(Throwable throwable) {
        //TODO
    }

    @Override
    public void returnRequestRetrofitNull(String weatherNull) {
        //TODO
    }

    @Override
    public void insertCityDatabase() {
        //TODO: перевести WeatherView в WeatherHistorySearch и обращаться отсюда к WeatherSource
    }

    @Override
    public void deleteCityDataBase() {

    }

    @Override
    public void updateCityDataBase() {

    }

    @Override
    public void getCityDataBaseById() {

    }

    @Override
    public List<WeatherView> getAllWeatherHistory() {
        return null;
    }
}
