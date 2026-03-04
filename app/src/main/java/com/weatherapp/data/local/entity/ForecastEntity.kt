package com.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "forecast",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ForecastEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val locationId: String,

    val dateTime: Long,

    val temperature: Double,

    val description: String,

    val icon: String,

    val humidity: Int,

    val windSpeed: Double
)