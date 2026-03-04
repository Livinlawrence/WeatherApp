package com.weatherapp.ui.state


import com.weatherapp.domain.model.Weather

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
    val condition: WeatherCondition = WeatherCondition.SUNNY,
    val forecast: List<ForecastUi> = emptyList()
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