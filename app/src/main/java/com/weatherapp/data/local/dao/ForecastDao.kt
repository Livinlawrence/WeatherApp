package com.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weatherapp.data.local.entity.ForecastEntity

@Dao
interface ForecastDao {

    @Query("SELECT * FROM forecast WHERE locationId = :locationId")
    suspend fun getForecast(locationId: String): List<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(list: List<ForecastEntity>)

    @Query("DELETE FROM forecast")
    suspend fun clearForecast()
}