package com.example.appweathergb.singleton;

import com.example.appweathergb.entities.WeatherView;

import java.util.ArrayList;
import java.util.List;

public class SimpleSingleton {
    //TODO переписать SimpleSingleton на MyApp

    private static final Object object = new Object();

    private static SimpleSingleton instance;

    private List<String> msg;
    private String city;
    private WeatherView weatherView;

    public synchronized WeatherView getWeatherView() {
        return weatherView;
    }

    public synchronized void setWeatherView(WeatherView weatherView) {
        this.weatherView = weatherView;
    }

    public synchronized List<String> getMsg() {
        return msg;
    }

    public synchronized String getCity() {
        return city;
    }

    public synchronized void setCity(String city) {
        this.city = city;
    }

    public synchronized void setMsg(List<String> msg) {
        this.msg = new ArrayList<>(msg);
    }

    private SimpleSingleton() {
    }

    public static SimpleSingleton getInstance() {
        synchronized (object) {
            if (instance == null) {
                instance = new SimpleSingleton();
            }
            return instance;
        }
    }
}
