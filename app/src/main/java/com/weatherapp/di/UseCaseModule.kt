package com.weatherapp.di

import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.usecase.AddFavoriteUseCase
import com.weatherapp.domain.usecase.GetCurrentLocationForecastUseCase
import com.weatherapp.domain.usecase.GetCurrentLocationWeatherUseCase
import com.weatherapp.domain.usecase.GetFavoritesUseCase
import com.weatherapp.domain.usecase.GetForecastUseCase
import com.weatherapp.domain.usecase.GetWeatherUseCase
import com.weatherapp.domain.usecase.RemoveFavoriteUseCase
import com.weatherapp.domain.usecase.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideWeatherUseCases(
        repository: WeatherRepository
    ): WeatherUseCases {

        return WeatherUseCases(
            getWeather = GetWeatherUseCase(repository),
            getForecast = GetForecastUseCase(repository),
            getFavorites = GetFavoritesUseCase(repository),
            addFavorite = AddFavoriteUseCase(repository),
            removeFavorite = RemoveFavoriteUseCase(repository),
            currentLocationWeather = GetCurrentLocationWeatherUseCase(repository),
            currentLocationForecast = GetCurrentLocationForecastUseCase(repository),
        )
    }
}