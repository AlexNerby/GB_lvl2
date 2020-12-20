package com.example.appweathergb.parser;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.service.MyWorker;
import com.example.appweathergb.storage.Constants;

public class JsonParser implements ParserConnector {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherJsonParser";
    private final static int subtract = 273;

    @Override
    public void parse(WeatherRequest weatherRequest, Context context, String exception) {
        if (LOG) {
            Log.v(TAG, "ParserImpl n Exception == " + exception);
        }

        final ParserConnector.BackParser mainActivityConnector = (ParserConnector.BackParser) context;
        final ParserConnector.BackParser workerConnector = new MyWorker.WorkerConnector();
        final WeatherView weatherView = new WeatherView();
        final Activity activity = (Activity) context;

        if (weatherRequest.getName() != null) {
            if (LOG) {
                Log.v(TAG, "weatherRequest != null");
            }
            weatherView.setCity(weatherRequest.getName());
            weatherView.setTemperature(String.valueOf(convert(weatherRequest)) + Constants.celsius);
            weatherView.setWindSpeed(String.valueOf(weatherRequest.getWind().getSpeed()));
            weatherView.setWorkerTemperature((int) (weatherRequest.getMain().getTemp() - subtract));
            weatherView.setException(exception);
        }

        new Thread(() -> {
            if (LOG) {
                Log.v(TAG, "run new Thread workerConnector");
            }
            workerConnector.dataWeather(weatherView, exception);
        }).start();

        activity.runOnUiThread(() -> {
            if (LOG) {
                Log.v(TAG, "runOnUiThread mainActivityConnector");
            }
            mainActivityConnector.dataWeather(weatherView, exception);
        });
    }

    private int convert(WeatherRequest weatherRequest) {
        float a = weatherRequest.getMain().getTemp() - subtract;
        int result = Math.round(a);
        return result;
    }
}
