package com.weatherapp.data.local.source

import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.local.entity.WeatherEntity
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao
) {

    suspend fun getWeather(locationId: String): WeatherEntity? {
        return weatherDao.getWeather(locationId)
    }

    suspend fun saveWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

    suspend fun clearWeather() {
        weatherDao.clearWeather()
    }

    suspend fun getForecast(locationId: String): List<ForecastEntity> {
        return forecastDao.getForecast(locationId)
    }

    suspend fun saveForecast(list: List<ForecastEntity>) {
        forecastDao.insertForecast(list)
    }

    suspend fun clearForecast() {
        forecastDao.clearForecast()
    }
}