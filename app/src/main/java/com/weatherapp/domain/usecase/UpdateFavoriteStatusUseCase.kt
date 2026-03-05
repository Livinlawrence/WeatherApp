package com.weatherapp.domain.usecase

import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class UpdateFavoriteStatusUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(locationId: String, isFavorite: Boolean) {
        repository.updateFavoriteStatus(locationId, isFavorite)
    }
}