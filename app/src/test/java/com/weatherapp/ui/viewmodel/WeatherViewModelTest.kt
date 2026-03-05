package com.weatherapp.ui.viewmodel

import com.weatherapp.MainDispatcherRule
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.usecase.WeatherUseCases
import com.weatherapp.ui.state.WeatherCondition
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCases: WeatherUseCases = mockk()
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        viewModel = WeatherViewModel(useCases)
    }

    @Test
    fun `loadWeather success updates uiState with weather and forecast`() = runTest {
        val mockWeather = mockk<Weather>(relaxed = true) {
            every { description } returns "scattered clouds"
            every { locationId } returns "loc_1"
        }
        coEvery { useCases.currentLocationWeather() } returns mockWeather
        coEvery { useCases.currentLocationForecast() } returns emptyList()
        coEvery { useCases.isFavorite("loc_1") } returns true
        viewModel.loadWeather()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(mockWeather, state.weather)
        assertTrue(state.isFavorite)
        assertEquals(WeatherCondition.CLOUDY, state.condition)
    }

    @Test
    fun `loadWeather failure updates uiState with error message`() = runTest {

        val errorMessage = "Network Timeout"
        coEvery { useCases.currentLocationWeather() } throws Exception(errorMessage)
        viewModel.loadWeather()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `loadWeatherForTheSelectedLocation loads weather`() = runTest {
        val weather = mockk<Weather>(relaxed = true) {
            every { description } returns "scattered clouds"
            every { locationId } returns "loc_1"
        }
        coEvery { useCases.getWeather("1", 10.0, 20.0) } returns weather
        coEvery { useCases.getForecast("1", 10.0, 20.0) } returns emptyList()
        coEvery { useCases.isFavorite("loc_1") } returns false
        viewModel.loadWeatherForTheSelectedLocation("1", 10.0, 20.0)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(weather, state.weather)
        assertFalse(state.isLoading)
    }

    @Test
    fun `toggleFavorite updates status and calls usecase`() = runTest {
        val mockWeather = mockk<Weather>(relaxed = true) {
            every { locationId } returns "loc_1"
        }
        coEvery { useCases.currentLocationWeather() } returns mockWeather
        coEvery { useCases.currentLocationForecast() } returns emptyList()
        coEvery { useCases.isFavorite(any()) } returns false
        coEvery { useCases.updateFavoriteStatusUseCase(any(), any()) } just Runs
        viewModel.loadWeather()
        assertFalse(viewModel.uiState.value.isFavorite)
        viewModel.toggleFavorite()
        assertTrue(viewModel.uiState.value.isFavorite)
        coVerify { useCases.updateFavoriteStatusUseCase("loc_1", true) }
    }

    @Test
    fun `toggleFavoritesDialog(true) triggers loadFavorites`() = runTest {
        coEvery { useCases.getFavorites() } returns emptyList()
        viewModel.toggleFavoritesDialog(true)
        assertTrue(viewModel.uiState.value.showFavoritesDialog)
        coVerify { useCases.getFavorites() }
    }
}