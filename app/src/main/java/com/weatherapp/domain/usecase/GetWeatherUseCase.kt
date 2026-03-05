package com.weatherapp.domain.usecase


import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        locationId: String,
        lat: Double,
        lon: Double
    ): Weather {
        return repository.getWeather(
            locationId = locationId,
            lat = lat,
            lon = lon
        )
    }
}