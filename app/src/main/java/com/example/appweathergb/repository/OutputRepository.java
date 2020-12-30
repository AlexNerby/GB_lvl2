package com.example.appweathergb.repository;

import com.example.appweathergb.network.model.WeatherRequest;

public interface OutputRepository {

    void returnGeoLocation(String lat, String lon);

    void returnNotPermission();

    void returnRequestRetrofit(WeatherRequest weatherRequest);

    void returnRequestRetrofitException(Throwable throwable);

    void returnRequestRetrofitNull(String weatherNull);

}
