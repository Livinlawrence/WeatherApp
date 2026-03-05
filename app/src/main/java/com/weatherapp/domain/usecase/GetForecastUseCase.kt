package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        locationId: String,
        lat: Double,
        lon: Double
    ): List<Forecast> {
        return repository.getForecast(
            locationId = locationId,
            lat = lat,
            lon = lon
        )
    }
}