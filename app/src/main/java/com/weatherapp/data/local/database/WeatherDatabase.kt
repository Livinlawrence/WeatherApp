package com.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.LocationDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class,
        LocationEntity::class
    ],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    abstract fun forecastDao(): ForecastDao

    abstract fun locationDao(): LocationDao
}