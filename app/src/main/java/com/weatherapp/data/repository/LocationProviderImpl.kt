package com.weatherapp.data.repository

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.weatherapp.domain.model.DeviceLocation
import com.weatherapp.domain.repository.LocationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): DeviceLocation =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            DeviceLocation(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    } else {
                        continuation.resumeWithException(
                            Exception("Location unavailable")
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
}