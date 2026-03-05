package com.weatherapp.data.remote.dto

data class ForecastResponseDto(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItemDto>,
    val city: CityDto
)

data class ForecastItemDto(
    val dt: Long,
    val main: ForecastMainDto,
    val weather: List<WeatherDto>,
    val clouds: CloudsDto,
    val wind: WindDto,
    val visibility: Int,
    val pop: Double,
    val rain: ForecastRainDto?,
    val sys: ForecastSysDto,
    val dt_txt: String
)

data class ForecastMainDto(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class ForecastRainDto(
    val `3h`: Double?
)

data class ForecastSysDto(
    val pod: String
)

data class CityDto(
    val id: Long,
    val name: String,
    val coord: CoordDto,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)