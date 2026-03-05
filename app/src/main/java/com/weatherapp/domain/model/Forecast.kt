package com.weatherapp.domain.model

data class Forecast(
    val locationId: String,
    val dateTime: Long,
    val temperature: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double
)