package com.example.appweathergb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appweathergb.entities.model.WeatherHistorySearch;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherHistorySearch weatherHistorySearch);

    @Update
    void updateWeather(WeatherHistorySearch weatherHistorySearch);

    @Delete
    void  deleteWeather(WeatherHistorySearch weatherHistorySearch);

    @Query("DELETE FROM WeatherHistorySearch WHERE id = :id")
    void deleteWeatherById(long id);

    @Query("SELECT * FROM WeatherHistorySearch")
    List<WeatherHistorySearch> getAllWeather();

    @Query("SELECT * FROM WeatherHistorySearch WHERE id = :id")
    WeatherHistorySearch getWeatherById(long id);

    @Query("SELECT COUNT() FROM WeatherHistorySearch")
    long getCountWeather();


}
