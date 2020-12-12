package com.example.appweathergb.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.appweathergb.BuildConfig;
import com.example.appweathergb.Constants;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.observers.WeatherConnector;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class JsonConnector implements Constants {
    //:TODO разобраться с имеющимся API или заменить на более подробный

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherJsonConnector";
    private int readTimeout = 10000;

    private WeatherConnector weatherConnector;
    private WeatherRequest weatherRequest;
    private String city;


    public JsonConnector(String city, Context context) {
        if (LOG) {
            Log.d(TAG, "Constructor JsonConnector");
        }

        if (context instanceof WeatherConnector) {
            if (weatherConnector == null) {
                weatherConnector = (WeatherConnector) context;
            }
            this.city = city;
            readJsonConnector();
        }
    }

    public void readJsonConnector() {
        if (LOG) {
            Log.v(TAG, "readJsonConnector");
        }
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
                    urlConnection.setRequestMethod(requestMethodGet);
                    urlConnection.setReadTimeout(readTimeout);

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
                    Log.e(TAG, failConnection, e);
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            weatherConnector.update(weatherRequest, failConnection);
                        }
                    });
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, failURI, e);
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader in) {
        if (LOG) {
            Log.v(TAG, "getLines");
        }

        return in.lines().collect(Collectors.joining("\n"));
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        if (LOG) {
            Log.v(TAG, "displayWeather");
        }
        weatherConnector.update(weatherRequest, null);
    }
}
