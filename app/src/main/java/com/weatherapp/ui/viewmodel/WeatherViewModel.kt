package com.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.model.Weather
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
        executeWeatherTask {
            val weather = useCases.currentLocationWeather()
            val forecast = useCases.currentLocationForecast()
            Pair(weather, forecast)
        }
    }

    fun loadWeatherForTheSelectedLocation(id: String?, lat: Double?, lon: Double?) {
        if (id == null || lat == null || lon == null) return
        _uiState.value = _uiState.value.copy(isFavorite = false)
        executeWeatherTask {
            val weather = useCases.getWeather(
                locationId = id, lat = lat, lon = lon
            )
            val forecast = useCases.getForecast(
                locationId = id, lat = lat, lon = lon
            )
            Pair(weather, forecast)
        }
    }


    private fun executeWeatherTask(task: suspend () -> Pair<Weather, List<Forecast>>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val (weather, forecast) = task()
                val isFavorite = useCases.isFavorite(weather.locationId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = weather,
                    isFavorite = isFavorite,
                    condition = mapCondition(weather.description),
                    forecast = forecast.map { it.toUi() })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun mapCondition(description: String): WeatherCondition {
        return when {
            description.contains("rain", true) -> WeatherCondition.RAINY
            description.contains("cloud", true) -> WeatherCondition.CLOUDY
            else -> WeatherCondition.SUNNY
        }
    }

    fun toggleFavorite() {
        val currentWeather = uiState.value.weather ?: return
        val isCurrentlyFavorite = uiState.value.isFavorite

        viewModelScope.launch {
            try {
                useCases.updateFavoriteStatusUseCase(
                    locationId = currentWeather.locationId, isFavorite = !isCurrentlyFavorite
                )
                _uiState.value = _uiState.value.copy(isFavorite = !isCurrentlyFavorite)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleFavoritesDialog(show: Boolean) {
        if (show) loadFavorites()
        _uiState.value = _uiState.value.copy(showFavoritesDialog = show)
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = useCases.getFavorites()
                _uiState.value = _uiState.value.copy(favorites = favorites)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            useCases.updateFavoriteStatusUseCase(locationId = id, isFavorite = false)
            loadFavorites() // Refresh the current fav list
            if (_uiState.value.weather?.locationId == id) {
                _uiState.value = _uiState.value.copy(isFavorite = false)
            }
        }
    }
}