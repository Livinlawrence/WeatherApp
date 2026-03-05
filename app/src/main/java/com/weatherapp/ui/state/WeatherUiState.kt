package com.weatherapp.ui.state


import com.weatherapp.domain.model.Location
import com.weatherapp.domain.model.Weather

data class WeatherUiState(
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
    val condition: WeatherCondition = WeatherCondition.SUNNY,
    val forecast: List<ForecastUi> = emptyList(),
    val favorites: List<Location> = emptyList(),
    val showFavoritesDialog: Boolean = false
)

data class ForecastUi(
    val day: String,
    val temperature: Int,
    val condition: WeatherCondition
)

enum class WeatherCondition {
    SUNNY,
    CLOUDY,
    RAINY
}