package com.weatherapp.data.remote.source

import com.weatherapp.data.remote.api.WeatherApi
import com.weatherapp.data.remote.dto.ForecastResponseDto
import com.weatherapp.data.remote.dto.WeatherResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRemoteDataSourceTest {

    private lateinit var api: WeatherApi
    private lateinit var dataSource: WeatherRemoteDataSource

    @Before
    fun setup() {

        api = mockk()

        dataSource = WeatherRemoteDataSource(api)
    }

    @Test
    fun `getCurrentWeather calls api and returns response`() = runTest {

        val response = mockk<WeatherResponseDto>()

        coEvery {
            api.getCurrentWeather(44.0, 10.0)
        } returns response

        val result = dataSource.getCurrentWeather(
            lat = 44.0,
            lon = 10.0
        )

        assertEquals(response, result)

        coVerify {
            api.getCurrentWeather(44.0, 10.0)
        }
    }

    @Test
    fun `getForecast calls api and returns response`() = runTest {

        val response = mockk<ForecastResponseDto>()

        coEvery {
            api.getForecast(44.0, 10.0)
        } returns response

        val result = dataSource.getForecast(
            lat = 44.0,
            lon = 10.0
        )

        assertEquals(response, result)

        coVerify {
            api.getForecast(44.0, 10.0)
        }
    }
}