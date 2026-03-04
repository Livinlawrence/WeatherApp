package com.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val country: String,

    val latitude: Double,

    val longitude: Double,

    val isFavorite: Boolean
)