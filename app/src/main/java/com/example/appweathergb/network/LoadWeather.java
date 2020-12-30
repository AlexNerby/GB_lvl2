package com.example.appweathergb.network;

import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.repository.InputLoadWeather;
import com.example.appweathergb.repository.OutputRepository;
import com.example.appweathergb.repository.WeatherRepository;
import com.example.appweathergb.singleton.MyApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadWeather implements InputLoadWeather {

    private final OutputRepository outputRepository;

    public LoadWeather(WeatherRepository weatherRepository) {
        outputRepository = weatherRepository;
    }

    @Override
    public void requestRetrofitByCoordinates(String lat, String lon, String key) {
        MyApp.getOpenWeatherApi().loadWeatherByCoordinates(lat, lon, key).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.body() != null) {
                    outputRepository.returnRequestRetrofit(response.body());
                } else {
                    outputRepository.returnRequestRetrofitNull(null);
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                outputRepository.returnRequestRetrofitException(t);
            }
        });
    }

    @Override
    public void requestRetrofitByCity(String city, String key) {
        MyApp.getOpenWeatherApi().loadWeather(city, key).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.body() != null) {
                    outputRepository.returnRequestRetrofit(response.body());
                } else {
                    outputRepository.returnRequestRetrofitNull(null);
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                outputRepository.returnRequestRetrofitException(t);
            }
        });
    }
}
