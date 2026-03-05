package com.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.weatherapp.core.util.Constants.DB_NAME
import com.weatherapp.data.local.dao.ForecastDao
import com.weatherapp.data.local.dao.LocationDao
import com.weatherapp.data.local.dao.WeatherDao
import com.weatherapp.data.local.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {

        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideWeatherDao(
        database: WeatherDatabase
    ): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    fun provideForecastDao(
        database: WeatherDatabase
    ): ForecastDao {
        return database.forecastDao()
    }

    @Provides
    fun provideLocationDao(
        database: WeatherDatabase
    ): LocationDao {
        return database.locationDao()
    }
}