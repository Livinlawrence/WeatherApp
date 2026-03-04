package com.weatherapp.domain.model

data class Weather(
    val locationId: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val timestamp: Long
)