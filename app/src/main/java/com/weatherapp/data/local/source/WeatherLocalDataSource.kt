package com.weatherapp.data.local.source

import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.LocationDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.entity.WeatherEntity
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao,
    private val locationDao: LocationDao
) {

    suspend fun getWeather(locationId: String): WeatherEntity? {
        return weatherDao.getWeather(locationId)
    }

    suspend fun saveWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

    suspend fun saveWeatherForLocation(location: LocationEntity, weather: WeatherEntity) {
        weatherDao.insertWeatherWithLocation(location, weather)
    }

    suspend fun getForecast(locationId: String): List<ForecastEntity> {
        return forecastDao.getForecast(locationId)
    }

    suspend fun saveForecast(list: List<ForecastEntity>) {
        forecastDao.insertForecast(list)
    }

    suspend fun updateFavoriteStatus(locationId: String, isFavorite: Boolean) {
        locationDao.updateFavoriteStatus(locationId,isFavorite)
    }

    suspend fun isFavorite(locationId: String): Boolean {
        return locationDao.isLocationFavorite(locationId)
    }

    suspend fun getFavorites(): List<LocationEntity> {
        return locationDao.getFavorites()
    }

    suspend fun deleteLocation(id: String) {
        locationDao.deleteLocation(id)
    }
}