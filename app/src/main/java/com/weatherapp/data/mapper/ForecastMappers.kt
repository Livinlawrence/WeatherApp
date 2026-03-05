package com.weatherapp.data.mapper

import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.remote.dto.ForecastItemDto
import com.weatherapp.domain.model.Forecast

fun ForecastItemDto.toDomain(locationId: String): Forecast {

    val weatherInfo = weather.first()

    return Forecast(
        locationId = locationId,
        dateTime = dt,
        temperature = main.temp,
        description = weatherInfo.description,
        icon = weatherInfo.icon,
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}

fun Forecast.toEntity(locationId: String): ForecastEntity =
    ForecastEntity(
        locationId = locationId,
        dateTime = dateTime,
        temperature = temperature,
        description = description,
        icon = icon,
        humidity = humidity,
        windSpeed = windSpeed
    )

fun ForecastEntity.toDomain(): Forecast =
    Forecast(
        locationId = locationId,
        dateTime = dateTime,
        temperature = temperature,
        description = description,
        icon = icon,
        humidity = humidity,
        windSpeed = windSpeed
    )