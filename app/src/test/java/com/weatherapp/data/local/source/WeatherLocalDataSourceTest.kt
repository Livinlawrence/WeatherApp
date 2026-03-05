package com.weatherapp.data.local.source

import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.LocationDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.local.entity.WeatherEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherLocalDataSourceTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var forecastDao: ForecastDao
    private lateinit var locationDao: LocationDao
    private lateinit var dataSource: WeatherLocalDataSource

    private val locationId = "place123"

    @Before
    fun setup() {
        weatherDao = mockk()
        forecastDao = mockk()
        locationDao = mockk()

        dataSource = WeatherLocalDataSource(
            weatherDao,
            forecastDao,
            locationDao
        )
    }

    @Test
    fun `getWeather returns weather from dao`() = runTest {

        val entity = WeatherEntity(
            locationId = locationId,
            temperature = 298.4,
            minTemp = 298.4,
            maxTemp = 298.4,
            feelsLike = 299.0,
            description = "moderate rain",
            name = "10d",
            humidity = 64,
            pressure = 1015,
            windSpeed = 0.62,
            timestamp = 123456,
        )

        coEvery { weatherDao.getWeather(locationId) } returns entity

        val result = dataSource.getWeather(locationId)

        assertEquals(entity, result)

        coVerify { weatherDao.getWeather(locationId) }
    }

    @Test
    fun `saveWeather calls dao insert`() = runTest {

        val entity = WeatherEntity(
            locationId = locationId,
            temperature = 298.4,
            feelsLike = 299.0,
            minTemp = 298.4,
            maxTemp = 298.4,
            description = "rain",
            name = "10d",
            humidity = 64,
            pressure = 1015,
            windSpeed = 0.62,
            timestamp = 123456
        )

        coEvery { weatherDao.insertWeather(entity) } just Runs

        dataSource.saveWeather(entity)

        coVerify { weatherDao.insertWeather(entity) }
    }

    @Test
    fun `getForecast returns forecast list`() = runTest {

        val forecast = listOf(
            ForecastEntity(
                id = 1,
                locationId = locationId,
                dateTime = 123,
                temperature = 296.7,
                description = "light rain",
                icon = "10d",
                humidity = 70,
                windSpeed = 1.2
            )
        )

        coEvery { forecastDao.getForecast(locationId) } returns forecast

        val result = dataSource.getForecast(locationId)

        assertEquals(forecast, result)

        coVerify { forecastDao.getForecast(locationId) }
    }

    @Test
    fun `saveForecast calls dao insert`() = runTest {

        val forecast = listOf(
            ForecastEntity(
                id = 1,
                locationId = locationId,
                dateTime = 123,
                temperature = 296.7,
                description = "rain",
                icon = "10d",
                humidity = 70,
                windSpeed = 1.2
            )
        )

        coEvery { forecastDao.insertForecast(forecast) } just Runs

        dataSource.saveForecast(forecast)

        coVerify { forecastDao.insertForecast(forecast) }
    }
}