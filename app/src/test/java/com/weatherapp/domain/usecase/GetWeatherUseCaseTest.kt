package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {
    private val repository: WeatherRepository = mockk()
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @Before
    fun setup() {
        getWeatherUseCase = GetWeatherUseCase(repository)
    }

    @Test
    fun `invoke should return weather from repository`() = runTest {
        val expectedWeather = mockk<Weather>()
        coEvery { repository.getWeather("id", 1.0, 2.0) } returns expectedWeather
        val result = getWeatherUseCase("id", 1.0, 2.0)
        assertEquals(expectedWeather, result)
        coVerify(exactly = 1) { repository.getWeather("id", 1.0, 2.0) }
    }
}