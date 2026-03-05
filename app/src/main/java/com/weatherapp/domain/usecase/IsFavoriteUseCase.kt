package com.weatherapp.domain.usecase


import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationId: String): Boolean {
        return repository.isFavorite(locationId)
    }
}