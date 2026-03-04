package com.weatherapp.di

import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.source.WeatherLocalDataSource
import com.weatherapp.data.remote.api.WeatherApi
import com.weatherapp.data.remote.source.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideWeatherRemoteDataSource(
        api: WeatherApi
    ): WeatherRemoteDataSource {

        return WeatherRemoteDataSource(api)
    }

    @Provides
    fun provideWeatherLocalDataSource(
        weatherDao: WeatherDao,
        forecastDao: ForecastDao
    ): WeatherLocalDataSource {

        return WeatherLocalDataSource(
            weatherDao,
            forecastDao
        )
    }
}