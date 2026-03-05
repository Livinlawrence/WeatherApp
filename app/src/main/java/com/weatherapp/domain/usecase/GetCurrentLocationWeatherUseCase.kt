package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentLocationWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Weather {
        return repository.getCurrentLocationWeather()
    }
}