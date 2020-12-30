package com.example.appweathergb.interact;

import com.example.appweathergb.entities.WeatherView;

import java.util.List;

public interface InputRepository {
    void searchByCity(String city);

    void searchByCoordinates();


    void insertCityDatabase();

    void deleteCityDataBase();

    void updateCityDataBase();

    void getCityDataBaseById();

    List<WeatherView> getAllWeatherHistory();


}
