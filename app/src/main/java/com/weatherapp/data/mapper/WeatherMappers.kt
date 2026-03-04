package com.weatherapp.data.mapper

import com.weatherapp.data.local.entity.WeatherEntity
import com.weatherapp.data.remote.dto.WeatherResponseDto
import com.weatherapp.domain.model.Weather

fun WeatherResponseDto.toDomain(locationId: String): Weather {

    val weatherInfo = weather.first()

    return Weather(
        locationId = locationId,
        temperature = main.temp,
        feelsLike = main.feels_like,
        description = weatherInfo.description,
        icon = weatherInfo.icon,
        humidity = main.humidity,
        pressure = main.pressure,
        windSpeed = wind.speed,
        timestamp = dt
    )
}

fun Weather.toEntity(): WeatherEntity =
    WeatherEntity(
        locationId = locationId,
        temperature = temperature,
        feelsLike = feelsLike,
        description = description,
        icon = icon,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        timestamp = timestamp
    )

fun WeatherEntity.toDomain(): Weather =
    Weather(
        locationId = locationId,
        temperature = temperature,
        feelsLike = feelsLike,
        description = description,
        icon = icon,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        timestamp = timestamp
    )