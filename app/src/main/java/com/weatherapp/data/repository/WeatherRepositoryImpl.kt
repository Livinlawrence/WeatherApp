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
            return getWeather(
                locationId = locationId,
                lat = location.latitude,
                lon = location.longitude
            )
        } catch (e: Exception) {
            e.printStackTrace()
            val cached = local.getWeather(locationId)
            cached?.toDomain() ?: throw e
        }
    }

    override suspend fun getCurrentLocationForecast(): List<Forecast> {
        val location = locationProvider.getCurrentLocation()
        return getForecast(
            locationId = "current_location",
            lat = location.latitude,
            lon = location.longitude
        )
    }

    override suspend fun getWeather(
        locationId: String, lat: Double, lon: Double
    ): Weather {
        return try {
            val response = remote.getCurrentWeather(lat, lon)
            val weather = response.toDomain(locationId)
            val locationEntity = LocationEntity(
                id = locationId,
                name = response.name,
                latitude = lat,
                longitude = lon
            )
            local.saveWeatherForLocation(
                location = locationEntity,
                weather = weather.toEntity()
            )
            weather
        } catch (e: Exception) {
            e.printStackTrace()
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
            }.groupBy { it.dateTime / 86400 }
                .map { it.value.first() }
                .take(5)
            local.saveForecast(
                forecasts.map { it.toEntity(locationId) })
            forecasts

        } catch (e: Exception) {
            e.printStackTrace()
            local.getForecast(locationId).take(5).map { it.toDomain() }
        }
    }

    override suspend fun updateFavoriteStatus(locationId: String, isFavorite: Boolean) {
        local.updateFavoriteStatus(
            locationId, isFavorite
        )
    }

    override suspend fun getFavorites(): List<Location> {
        return local.getFavorites().map { it.toDomain() }
    }

    override suspend fun removeFavorite(locationId: String) {
        local.deleteLocation(locationId)
    }

    override suspend fun isFavorite(locationId: String): Boolean {
        return local.isFavorite(locationId)
    }
}