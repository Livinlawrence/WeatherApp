package com.weatherapp.domain.repository

import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.model.Location
import com.weatherapp.domain.model.Weather

interface WeatherRepository {

    suspend fun getCurrentLocationWeather(): Weather
    suspend fun getCurrentLocationForecast(): List<Forecast>

    suspend fun getWeather(
        locationId: String,
        lat: Double,
        lon: Double
    ): Weather

    suspend fun getForecast(
        locationId: String,
        lat: Double,
        lon: Double
    ): List<Forecast>

    suspend fun addFavorite(location: Location)

    suspend fun getFavorites(): List<Location>

    suspend fun removeFavorite(locationId: String)
}