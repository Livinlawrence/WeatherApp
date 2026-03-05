package com.weatherapp.data.mapper

import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.entity.WeatherEntity
import com.weatherapp.data.remote.dto.WeatherResponseDto
import com.weatherapp.domain.model.Location
import com.weatherapp.domain.model.Weather

fun WeatherResponseDto.toDomain(locationId: String): Weather {

    val weatherInfo = weather.first()

    return Weather(
        locationId = locationId,
        temperature = main.temp,
        minTemp = main.temp_min,
        maxTemp = main.temp_max,
        feelsLike = main.feels_like,
        description = weatherInfo.description,
        name = name,
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
        minTemp = minTemp,
        maxTemp = maxTemp,
        feelsLike = feelsLike,
        description = description,
        name = name,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        timestamp = timestamp
    )

fun WeatherEntity.toDomain(): Weather =
    Weather(
        locationId = locationId,
        temperature = temperature,
        minTemp = minTemp,
        maxTemp = maxTemp,
        feelsLike = feelsLike,
        description = description,
        name = name,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
        timestamp = timestamp
    )

fun LocationEntity.toDomain(): Location =
    Location(
        id = id,
        name = name,
        isFavorite = isFavorite,
        latitude = latitude,
        longitude = longitude
    )