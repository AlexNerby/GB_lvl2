package com.example.appweathergb.interact;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.appweathergb.R;
import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.repository.OutputInteractor;
import com.example.appweathergb.singleton.MyApp;
import com.example.appweathergb.storage.Constants;
import com.example.appweathergb.repository.WeatherRepository;
import com.example.appweathergb.ui.presenter.InputInteractor;
import com.example.appweathergb.network.model.WeatherRequest;

public class WeatherInteractor implements InputInteractor, OutputInteractor {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherInteractor";

    //TODO подключить поток(и)
    private final HandlerThread handlerThread;
    private final Handler handler;
    private final InputRepository inputRepository;
    private final OutputPresenter outputPresenter;

    public WeatherInteractor(OutputPresenter outputPresenter) {
        if (LOG) {
            Log.v(TAG, "Constructor");
        }
        inputRepository = new WeatherRepository(this);
        this.outputPresenter = outputPresenter;

        handlerThread = new HandlerThread("Interactor HandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public void searchByCity(String city) {
        if (LOG) {
            Log.v(TAG, "insert");
        }
        inputRepository.searchByCity(city);
    }

    @Override
    public void searchByCoordinates() {
        inputRepository.searchByCoordinates();
    }

    @Override
    public void returnWeatherRequest(WeatherRequest weatherRequest) {
        if (LOG) {
            Log.v(TAG, "returnWeatherRequest | temp == " + weatherRequest
                    .getMain().getTemp());
        }
        outputPresenter.insertWeatherView(parseWeatherRequest(weatherRequest));
    }

    private WeatherView parseWeatherRequest(WeatherRequest weatherRequest) {
        if (LOG) {
            Log.v(TAG, "parseWeatherRequest");
        }
        final WeatherView weatherView = new WeatherView();

        if (weatherRequest != null) {

            weatherView.setCity(weatherRequest.getName());
            weatherView.setTemperature(String.valueOf(convert(weatherRequest)) + Constants.celsius);
            weatherView.setWindSpeed(String.format(MyApp
                            .getAppContext()
                            .getString(R.string.wind_speed)
                    , String.valueOf(weatherRequest.getWind().getSpeed())));

            if (LOG) {
                Log.v(TAG, "weatherRequest != null | temp == " + weatherView.getTemperature());
            }
        } else {
            if (LOG) {
                Log.v(TAG, "parseWeatherRequest + " + Constants.failConnection);
            }
            weatherView.setCity(Constants.failConnection);
        }
        return weatherView;
    }

    private int convert(WeatherRequest weatherRequest) {
        if (LOG) {
            Log.v(TAG, "convert");
        }
        float a = weatherRequest.getMain().getTemp() - Constants.subtract;
        int result = Math.round(a);
        checkTemperature(result);
        return result;
    }

    private void checkTemperature(int result) {
        //TODO отловить температуру и отправить push
    }
}
