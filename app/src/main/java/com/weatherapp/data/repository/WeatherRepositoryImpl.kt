package com.weatherapp.data.repository

import com.weatherapp.data.local.entity.LocationEntity
import com.weatherapp.data.local.source.WeatherLocalDataSource
import com.weatherapp.data.mapper.toDomain
import com.weatherapp.data.mapper.toEntity
import com.weatherapp.data.remote.source.WeatherRemoteDataSource
import com.weatherapp.domain.model.Forecast
import com.weatherapp.domain.model.Location
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.LocationProvider
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remote: WeatherRemoteDataSource,
    private val local: WeatherLocalDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {

    override suspend fun getCurrentLocationWeather(): Weather {
        val location = locationProvider.getCurrentLocation()
        val locationId = "current_location"

        return try {
            val localLocation = local.getLocation(locationId)
            if (localLocation == null) {
                local.saveLocation(
                    LocationEntity(
                        id = locationId,
                        name = "Current Location",
                        country = "",
                        latitude = location.latitude,
                        longitude = location.longitude,
                        isFavorite = false
                    )
                )
            }

            return getWeather(locationId, location.latitude, location.longitude)

        } catch (e: Exception) {
            val cached = local.getWeather(locationId)
            cached?.toDomain() ?: throw e
        }
    }

    override suspend fun getCurrentLocationForecast(): List<Forecast> {
        val location = locationProvider.getCurrentLocation()
        val locationId = "current_location"
        return getForecast(locationId, location.latitude, location.longitude)
    }

    override suspend fun getWeather(
        locationId: String, lat: Double, lon: Double
    ): Weather {
        return try {
            val response = remote.getCurrentWeather(lat, lon)
            val weather = response.toDomain(locationId)
            local.saveWeather(
                weather.toEntity()
            )
            weather

        } catch (e: Exception) {
            val cached = local.getWeather(locationId)
            cached?.toDomain() ?: throw e
        }
    }

    override suspend fun getForecast(
        locationId: String, lat: Double, lon: Double
    ): List<Forecast> {
        return try {
            val response = remote.getForecast(lat, lon)
            val forecasts = response.list.map {
                it.toDomain(locationId)
            }
            local.saveForecast(
                forecasts.map { it.toEntity(locationId) })
            forecasts

        } catch (e: Exception) {
            local.getForecast(locationId).map { it.toDomain() }
        }
    }

    override suspend fun addFavorite(location: Location) {
        local.saveLocation(
            location.toEntity()
        )
    }

    override suspend fun getFavorites(): List<Location> {
        return local.getLocations().map { it.toDomain() }
    }

    override suspend fun removeFavorite(locationId: String) {
        local.deleteLocation(locationId)
    }
}