package com.example.appweathergb.model;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.appweathergb.BuildConfig;
import com.example.appweathergb.observers.WeatherConnector;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class JsonConnector {

    //:TODO разобраться с имеющимся API или заменить на более подробный

    private static final boolean LOG = true;
    private static final String TAG = "modelWeather";

    private WeatherConnector weatherConnector;
    private String city;
    private WeatherRequest weatherRequest;


    public JsonConnector(String city, Context context) {
        if (context instanceof WeatherConnector) {
            if (weatherConnector == null) {
                weatherConnector = (WeatherConnector) context;
            }
            this.city = city;
            readJsonConnector();
        }
    }

    public void readJsonConnector()
    {
        try {
            final String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s",
                    city, BuildConfig.WEATHER_API_KEY);
            final URL uri = new URL(url);
            final Handler handler = new Handler();

            new Thread(() -> {
            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result = getLines(in);

                Gson gson = new Gson();
                weatherRequest = gson.fromJson(result, WeatherRequest.class);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayWeather(weatherRequest);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Fail connection", e);
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        weatherConnector.update(weatherRequest, "Fail connection");
                    }
                });
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
            }
        }

    private static String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        weatherConnector.update(weatherRequest, null);
    }
}
