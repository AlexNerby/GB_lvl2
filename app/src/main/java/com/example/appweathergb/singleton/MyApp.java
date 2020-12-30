package com.example.appweathergb.singleton;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.room.Room;

import com.example.appweathergb.broadcast_receiver.BatteryLevelReceiver;
import com.example.appweathergb.broadcast_receiver.ConnectNetworkReceiver;
import com.example.appweathergb.dao.WeatherDao;
import com.example.appweathergb.database.WeatherDataBase;
import com.example.appweathergb.network.MyRetrofit;
import com.example.appweathergb.network.OpenWeather;


public class MyApp extends Application {

    private static MyApp INSTANCE;
    private static MyRetrofit retrofit;
    private WeatherDataBase db;
    private BroadcastReceiver geoLocationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        retrofit = new MyRetrofit();
        createNotificationChannel();
        initDataBase();
        initBroadcastReceivers();
    }

    public static OpenWeather getOpenWeatherApi() {
        return retrofit.getOpenWeather();
    }

    public static Context getAppContext() {
        return INSTANCE;
    }

    public static MyApp getInstance() {
        return INSTANCE;
    }

    private void initDataBase() {
        db = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDataBase.class,
                "weather_database2")
                .allowMainThreadQueries() //Только для примеров и тестирования.
                .build();
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }

    private void initBroadcastReceivers() {
        BroadcastReceiver batteryLevelReceiver = new BatteryLevelReceiver();
        IntentFilter filterBattery = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        this.registerReceiver(batteryLevelReceiver, filterBattery);

        BroadcastReceiver connectNetworkReceiver = new ConnectNetworkReceiver();
        IntentFilter filterNetwork = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(connectNetworkReceiver, filterNetwork);

        //TODO: next broadcasts
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "id2",
                    "My Weather",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
