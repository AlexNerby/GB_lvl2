package com.example.appweathergb.entities.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index(value = {"city"})})
public class WeatherHistorySearch {

    public WeatherHistorySearch(String city) {
        this.city = city;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "city")
    public String city;

    public Date dateWeather;

    @Override
    public String toString() {
        return city + "  " +
                 dateWeather;
    }

    //TODO: temperature and etc
}
