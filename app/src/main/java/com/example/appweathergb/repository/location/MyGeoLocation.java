package com.example.appweathergb.repository.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.appweathergb.repository.InputGeoLocation;
import com.example.appweathergb.repository.OutputRepository;
import com.example.appweathergb.repository.WeatherRepository;
import com.example.appweathergb.singleton.MyApp;

import static android.content.Context.LOCATION_SERVICE;

public class MyGeoLocation implements InputGeoLocation {
    private static final boolean LOG = true;
    private static final String TAG = "myWeatherGeoPositionService";

    private final OutputRepository outputRepository;

    public MyGeoLocation(WeatherRepository weatherRepository) {
        if (LOG) {
            Log.v(TAG, "constructor");
        }
        outputRepository = weatherRepository;
    }

    @Override
    public void startGeoLocation() {
        if (LOG) {
            Log.v(TAG, "startLocationService");
        }
        requestLocation();
    }

    // Запрос координат
    private void requestLocation() {
        if (LOG) {
            Log.v(TAG, "requestLocation");
        }
        // Если пермиссии все таки нет - то просто выйдем, приложение не имеет смысла
        if (ActivityCompat.checkSelfPermission(MyApp.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyApp.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (LOG) {
                Log.v(TAG, "requestLocation not permission");
            }
            outputRepository.returnNotPermission();
            return;
        }

        // Получить менеджер геолокаций
        LocationManager locationManager = (LocationManager) MyApp.getAppContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            if (LOG) {
                Log.v(TAG, "requestLocation provider != null");
            }
            // Будем получать геоположение через каждые 10 секунд или каждые 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (LOG) {
                        Log.v(TAG, "onLocationChanged");
                    }
                    String lat = String.valueOf(location.getLatitude());
                    String lon = String.valueOf(location.getLongitude());
                    outputRepository.returnGeoLocation(lat, lon);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }
}
