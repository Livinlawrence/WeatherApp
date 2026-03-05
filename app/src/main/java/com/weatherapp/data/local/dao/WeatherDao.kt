package com.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE locationId = :locationId")
    suspend fun getWeather(locationId: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    // We reference the LocationDao method here or define a local insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertLocation(location: LocationEntity)

    /**
     * This method ensures that the Location exists before the Weather is saved.
     * Using @Transaction makes this atomic: both succeed or both fail.
     */
    @Transaction
    open suspend fun insertWeatherWithLocation(location: LocationEntity, weather: WeatherEntity) {
        insertLocation(location)
        insertWeather(weather)
    }


    @Query("DELETE FROM weather")
    suspend fun clearWeather()
}