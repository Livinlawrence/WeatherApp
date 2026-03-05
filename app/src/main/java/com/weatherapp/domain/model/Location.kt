package com.weatherapp.domain.model

data class Location(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var isFavorite: Boolean? = false
)