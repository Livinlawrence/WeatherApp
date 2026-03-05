package com.weatherapp.data.remote.source

import com.weatherapp.data.remote.api.WeatherApi
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
     val api: WeatherApi
) {

    suspend fun getCurrentWeather(lat: Double, lon: Double) =
        api.getCurrentWeather(lat, lon)

    suspend fun getForecast(lat: Double, lon: Double) =
        api.getForecast(lat, lon)
}