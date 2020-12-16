package com.example.appweathergb.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.example.appweathergb.BuildConfig;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.parcer.JsonParser;
import com.example.appweathergb.parcer.ParserConnector;
import com.example.appweathergb.storage.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class JsonService extends Service {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherServiceGetJson";
    private static final int readTimeout = 10000;

    private final IBinder binder = new ServiceBinder();

    private final HandlerThread handlerThread = new HandlerThread("HandlerThreadJsonService");
    private final ParserConnector parserConnector = new JsonParser();
    private volatile WeatherRequest weatherRequest;

    @Override
    public void onCreate() {
        if (LOG) {
            Log.d(TAG, "onCreate | handlerThread.start()");
        }
        handlerThread.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (LOG) {
            Log.d(TAG, "onDestroy | handlerThread.quitSafely()");
        }
        handlerThread.quitSafely();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (LOG) {
            Log.d(TAG, "onBind");
        }
        return binder;
    }

    public void startHttps(Context context, String city) {
        if (LOG) {
            Log.v(TAG, "startHttps");
        }

        final Handler handler = new Handler(handlerThread.getLooper());
        final Handler handlerException = new Handler(handlerThread.getLooper());
        try {
            final String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s",
                    city, BuildConfig.WEATHER_API_KEY);
            final URL uri = new URL(url);

            handler.post(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod(Constants.requestMethodGet);
                    urlConnection.setReadTimeout(readTimeout);

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = getLines(in);

                    Gson gson = new Gson();
                    weatherRequest = gson.fromJson(result, WeatherRequest.class);
                    parserConnector.parse(weatherRequest, context, null);

                } catch (Exception e) {
                    Log.e(TAG, Constants.failConnection, e);
                    e.printStackTrace();
                    handlerException.post(new Runnable() {
                        @Override
                        public void run() {
                            parserConnector.parse(weatherRequest, context, Constants.failConnection);
                        }
                    });

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            });
        } catch (MalformedURLException e) {
            Log.e(TAG, Constants.failURI, e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        if (LOG) {
            Log.v(TAG, "getLines");
        }
        return in.lines().collect(Collectors.joining("\n"));
    }

    // Класс связи между клиентом и сервисом
    public class ServiceBinder extends Binder {
        JsonService getService() {
            return JsonService.this;
        }

        public void startHttps(Context context, String city) {
            getService().startHttps(context, city);
        }
    }
}
