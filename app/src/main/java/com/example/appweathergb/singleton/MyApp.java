package com.example.appweathergb.singleton;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.room.Room;

import com.example.appweathergb.dao.WeatherDao;
import com.example.appweathergb.database.WeatherDataBase;
import com.example.appweathergb.network.MyRetrofit;
import com.example.appweathergb.network.OpenWeather;

public class MyApp extends Application {

    private static MyApp INSTANCE;
    private static MyRetrofit retrofit;
    private WeatherDataBase db;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        retrofit = new MyRetrofit();
        createNotificationChannel();
        initDataBase();
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



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "id2",
                    "Какой то канал",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
