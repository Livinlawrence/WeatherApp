package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Location
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(): List<Location> {
        return repository.getFavorites()
    }
}