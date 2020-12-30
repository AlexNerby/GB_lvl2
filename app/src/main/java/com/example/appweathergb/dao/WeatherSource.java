package com.example.appweathergb.dao;

import com.example.appweathergb.database.WeatherDataBase;
import com.example.appweathergb.entities.model.WeatherHistorySearch;

import java.util.Calendar;
import java.util.List;

public class WeatherSource {

    //TODO: обращаться через WeatherRepository

    private final WeatherDao weatherDao;

    private List<WeatherHistorySearch> weatherHistories;
    private WeatherHistorySearch weatherHistorySearch;

    public WeatherSource(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public List<WeatherHistorySearch> getWeatherHistories() {
        if (weatherHistories == null) {
            LoadWeatherHistory();
        }
        return weatherHistories;
    }

    private void LoadWeatherHistory() {
        weatherHistories = weatherDao.getAllWeather();
    }

    // Получаем количество записей
    public long getCountWeathers(){
        return weatherDao.getCountWeather();
    }

    // Добавляем студента
    public void addWeatherHistory(WeatherHistorySearch weatherHistorySearch){
        Calendar cal = Calendar.getInstance();
        weatherHistorySearch.dateWeather = cal.getTime();
        weatherDao.insertWeather(weatherHistorySearch);
        LoadWeatherHistory();
    }

    // Заменяем студента
    public void updateWeather(WeatherHistorySearch weatherHistorySearch){
        weatherDao.updateWeather(weatherHistorySearch);
        LoadWeatherHistory();
    }

    // Удаляем студента из базы
    public void removeWeather(long id){
        weatherDao.deleteWeatherById(id);
        LoadWeatherHistory();
    }

    public WeatherHistorySearch getWeatherHistory(Long id) {
        weatherHistorySearch = weatherDao.getWeatherById(id);
        LoadWeatherHistory();
        return weatherHistorySearch;
    }


}
