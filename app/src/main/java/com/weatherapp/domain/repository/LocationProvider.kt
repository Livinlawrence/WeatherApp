package com.weatherapp.domain.repository

import com.weatherapp.domain.model.DeviceLocation

interface LocationProvider {

    suspend fun getCurrentLocation(): DeviceLocation
}