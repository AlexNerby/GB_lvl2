package com.example.appweathergb.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.appweathergb.dao.WeatherDao;
import com.example.appweathergb.entities.model.DateConverter;
import com.example.appweathergb.entities.model.WeatherHistorySearch;

@Database(entities = {WeatherHistorySearch.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class WeatherDataBase extends RoomDatabase {

    public abstract WeatherDao getWeatherDao();
}
