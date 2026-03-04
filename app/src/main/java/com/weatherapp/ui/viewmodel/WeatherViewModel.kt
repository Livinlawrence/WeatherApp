package com.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.domain.model.Location
import com.weatherapp.domain.usecase.WeatherUseCases
import com.weatherapp.ui.mapper.toUi
import com.weatherapp.ui.state.WeatherCondition
import com.weatherapp.ui.state.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val useCases: WeatherUseCases
) : ViewModel() {


    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val weather = useCases.currentLocationWeather()
                val forecast = useCases.currentLocationForecast()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = weather,
                    condition = mapCondition(weather.description),
                    forecast = forecast
                        .groupBy { it.dateTime / 86400 }
                        .map { it.value.first() }
                        .take(5)
                        .map { it.toUi() }
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun mapCondition(description: String): WeatherCondition {
        return when {
            description.contains("rain", true) -> WeatherCondition.RAINY
            description.contains("cloud", true) -> WeatherCondition.CLOUDY
            else -> WeatherCondition.SUNNY
        }
    }
}