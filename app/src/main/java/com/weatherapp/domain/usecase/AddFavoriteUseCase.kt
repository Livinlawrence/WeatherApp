package com.weatherapp.domain.usecase


import com.weatherapp.domain.model.Location
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(location: Location) {
        repository.addFavorite(location)
    }
}