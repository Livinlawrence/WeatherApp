package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentLocationForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke():  List<Forecast>  {
        return repository.getCurrentLocationForecast()
    }
}