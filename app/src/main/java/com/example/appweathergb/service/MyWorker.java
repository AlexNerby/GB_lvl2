package com.example.appweathergb.service;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.appweathergb.R;
import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.parser.ParserConnector;
import com.example.appweathergb.singleton.MyApp;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;


public class MyWorker extends Worker {

    private final static String TAG = "myWeatherWorker";
    private static final Object object = new Object();

    private static String expandedTitleText;
    private static String expandedInfoText;
    private final static String coldWeather = "https://images.unsplash.com/photo-1514632595-4944383f2737?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80";
    private final static String normalWeather = "https://images.unsplash.com/photo-1550143387-52fa147f9c65?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1048&q=80";
    private final static String warmWeather = "https://images.unsplash.com/photo-1472828420184-741d2c0d43b3?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1746&q=80";
    private static String containerUri;
    private final NotificationManagerCompat notificationManager;
    private final Handler handler;
    private final RemoteViews collapsedView;
    private final RemoteViews expandedView;


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d(TAG, "MyWorker constructor");

        notificationManager = NotificationManagerCompat.from(MyApp.getAppContext());
        handler = new Handler(Looper.getMainLooper());

        collapsedView = new RemoteViews(MyApp.getAppContext().getPackageName(),
                R.layout.notification_collapse);
        expandedView = new RemoteViews(MyApp.getAppContext().getPackageName(),
                R.layout.notification_expanded);
        expandedView.setTextViewText(R.id.notification_expnd_title, expandedTitleText);
        expandedView.setTextViewText(R.id.notification_expnd_info, expandedInfoText);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.v(TAG, "doWork");

        final Notification notification = new NotificationCompat.Builder(MyApp.getAppContext(), "id2")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Weather")
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();

        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "handler.post");

                Picasso.with(MyApp.getAppContext())
                        .load(containerUri)
                        .into(expandedView, R.id.remoteview_notification_icon, 3, notification);
                notificationManager.notify(3, notification);
            }
        });
        return Result.success();
    }


    public static class WorkerConnector implements ParserConnector.BackParser {

        public void createWorker() {
            Log.v(TAG, "createWorker");
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest
                    .Builder(MyWorker.class)
                    .setInitialDelay(2, TimeUnit.SECONDS)
                    .build();

            WorkManager workManager = WorkManager.getInstance();
            workManager.enqueue(workRequest);
        }

        @Override
        public void dataWeather(WeatherView weatherView, String exception) {

            synchronized (object) {

                int temp = weatherView.getWorkerTemperature();
                if (exception == null) {
                    Log.v(TAG, "dataWeather exception == null");

                    if (temp >= 0) {
                        containerUri = warmWeather;
                    } else if (temp < 0 && temp > (-5)) {
                        containerUri = normalWeather;
                    } else if (temp <= (-5)) {
                        containerUri = coldWeather;
                    }
                    expandedTitleText = weatherView.getCity();
                    expandedInfoText = weatherView.getTemperature();

                    createWorker();
                }
            }
        }
    }
}
