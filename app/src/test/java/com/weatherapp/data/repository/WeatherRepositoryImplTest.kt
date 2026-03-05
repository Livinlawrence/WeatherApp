package com.weatherapp.data.repository

import com.weatherapp.data.local.entity.ForecastEntity
import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.entity.WeatherEntity
import com.weatherapp.data.local.source.WeatherLocalDataSource
import com.weatherapp.data.remote.dto.ForecastResponseDto
import com.weatherapp.data.remote.dto.WeatherResponseDto
import com.weatherapp.data.remote.source.WeatherRemoteDataSource
import com.weatherapp.domain.repository.LocationProvider
import com.weatherapp.domain.repository.WeatherRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private lateinit var remote: WeatherRemoteDataSource
    private lateinit var local: WeatherLocalDataSource
    private lateinit var locationProvider: LocationProvider

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {

        remote = mockk()
        local = mockk()
        locationProvider = mockk()

        repository = WeatherRepositoryImpl(
            remote = remote,
            local = local,
            locationProvider = locationProvider
        )
    }


    @Test
    fun `getWeather fetches from remote and saves to local on success`() = runTest {
        val id = "test_id"
        val lat = 10.0
        val lon = 20.0
        val mockDto = mockWeatherResponseDto()

        coEvery { remote.getCurrentWeather(lat, lon) } returns mockDto
        coEvery { local.saveWeatherForLocation(any(), any()) } just Runs


        val result = repository.getWeather(id, lat, lon)


        assertEquals("London", result.name)
        assertEquals(25.0, result.temperature, 0.0)
        coVerify(exactly = 1) { remote.getCurrentWeather(lat, lon) }
        coVerify(exactly = 1) { local.saveWeatherForLocation(any(), any()) }
    }

    @Test
    fun `getWeather returns cached weather when remote fails`() = runTest {

        val locationId = "test_location"

        coEvery { remote.getCurrentWeather(any(), any()) } throws RuntimeException()
        val cached = mockWeatherEntity()
        coEvery { local.getWeather(locationId) } returns cached
        val result = repository.getWeather(
            locationId = locationId,
            lat = 44.0,
            lon = 10.0
        )

        assertEquals(cached.temperature, result.temperature)
        coVerify { local.getWeather(locationId) }
    }

    @Test
    fun `getForecast returns grouped forecasts and saves locally`() = runTest {

        val dto = mockForecastResponseDto()
        coEvery { remote.getForecast(44.0, 10.0) } returns dto
        coEvery { local.saveForecast(any()) } just Runs
        val result = repository.getForecast(
            locationId = "test",
            lat = 44.0,
            lon = 10.0
        )
        assertTrue(result.size <= 5)
        coVerify {
            remote.getForecast(44.0, 10.0)
            local.saveForecast(any())
        }
    }

    @Test
    fun `getForecast returns cached forecast when remote fails`() = runTest {

        coEvery { remote.getForecast(any(), any()) } throws RuntimeException()
        val cached = listOf(mockForecastEntity())
        coEvery { local.getForecast("test") } returns cached
        val result = repository.getForecast(
            locationId = "test",
            lat = 44.0,
            lon = 10.0
        )
        assertEquals(cached.size, result.size)
        coVerify { local.getForecast("test") }
    }

    @Test
    fun `updateFavoriteStatus calls local datasource`() = runTest {
        coEvery { local.updateFavoriteStatus("id", true) } just Runs
        repository.updateFavoriteStatus("id", true)
        coVerify { local.updateFavoriteStatus("id", true) }
    }

    @Test
    fun `getFavorites returns mapped favorites`() = runTest {
        val entities = listOf(mockLocationEntity())
        coEvery { local.getFavorites() } returns entities
        val result = repository.getFavorites()
        assertEquals(entities.size, result.size)
        coVerify { local.getFavorites() }
    }

    @Test
    fun `removeFavorite deletes location`() = runTest {
        coEvery { local.deleteLocation("id") } just Runs
        repository.removeFavorite("id")
        coVerify { local.deleteLocation("id") }
    }

    private fun mockWeatherResponseDto() =
        mockk<WeatherResponseDto>(relaxed = true) {
            every { name } returns "London"
            every { main } returns mockk(relaxed = true) { every { temp } returns 25.0 }
            every { weather } returns listOf(mockk(relaxed = true) { every { description } returns "Clear" })
        }

    private fun mockForecastResponseDto() =
        mockk<ForecastResponseDto>(relaxed = true) {
            every { cod } returns "Zocca"
            every { cnt } returns 2
        }
}

private fun mockWeatherEntity() =
    WeatherEntity(
        locationId = "test",
        temperature = 298.4,
        minTemp = 298.4,
        maxTemp = 298.4,
        feelsLike = 299.0,
        description = "sunny",
        name = "01d",
        humidity = 60,
        pressure = 1000,
        windSpeed = 2.0,
        timestamp = 1000L
    )

private fun mockForecastEntity() =
    ForecastEntity(
        id = 1,
        locationId = "test",
        dateTime = 123456,
        temperature = 25.0,
        description = "sunny",
        icon = "01d",
        humidity = 60,
        windSpeed = 2.0
    )


private fun mockLocationEntity() =
    LocationEntity(
        id = "test",
        name = "Zocca",
        latitude = 44.0,
        longitude = 10.0,
        isFavorite = true
    )
