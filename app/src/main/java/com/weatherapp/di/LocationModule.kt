package com.weatherapp.di

import com.weatherapp.data.repository.LocationProviderImpl
import com.weatherapp.domain.repository.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindLocationProvider(
        impl: LocationProviderImpl
    ): LocationProvider
}