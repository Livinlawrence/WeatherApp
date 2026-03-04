package com.weatherapp.data.remote.api

import com.weatherapp.BuildConfig
import com.weatherapp.data.remote.dto.ForecastResponseDto
import com.weatherapp.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponseDto

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String= BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ): ForecastResponseDto
}