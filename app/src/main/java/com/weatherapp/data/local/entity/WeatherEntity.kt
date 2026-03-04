package com.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "weather",foreignKeys = [
    ForeignKey(
        entity = LocationEntity::class,
        parentColumns = ["id"],
        childColumns = ["locationId"],
        onDelete = ForeignKey.CASCADE
    )
],indices = [Index(value = ["locationId"])])
data class WeatherEntity(
    @PrimaryKey
    val locationId: String,
    val temperature: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val feelsLike: Double,
    val description: String,
    val name: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val timestamp: Long
)