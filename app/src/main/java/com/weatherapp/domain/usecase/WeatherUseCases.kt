package com.weatherapp.domain.usecase


data class WeatherUseCases(
    val currentLocationWeather: GetCurrentLocationWeatherUseCase,
    val currentLocationForecast: GetCurrentLocationForecastUseCase,
    val getWeather: GetWeatherUseCase,
    val getForecast: GetForecastUseCase,
    val getFavorites: GetFavoritesUseCase,
    val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    val isFavorite: IsFavoriteUseCase
)