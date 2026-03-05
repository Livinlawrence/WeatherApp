package com.weatherapp.data.mapper

import com.weatherapp.data.remote.dto.CloudsDto
import com.weatherapp.data.remote.dto.CoordDto
import com.weatherapp.data.remote.dto.MainDto
import com.weatherapp.data.remote.dto.SysDto
import com.weatherapp.data.remote.dto.WeatherDto
import com.weatherapp.data.remote.dto.WeatherResponseDto
import com.weatherapp.data.remote.dto.WindDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeatherMapperTest {

    private fun createMockWeatherResponseDto(
        name: String = "London",
        temp: Double = 25.0,
        description: String = "scattered clouds"
    ) = WeatherResponseDto(
        coord = CoordDto(lon = -0.1257, lat = 51.5085),
        weather = listOf(
            WeatherDto(id = 802, main = "Clouds", description = description, icon = "03d")
        ),
        base = "stations",
        main = MainDto(
            temp = temp,
            feels_like = temp + 1.0,
            temp_min = temp - 2.0,
            temp_max = temp + 2.0,
            pressure = 1012,
            humidity = 50,
            sea_level = 1012,
            grnd_level = 1000
        ),
        visibility = 10000,
        wind = WindDto(speed = 4.12, deg = 80, gust = 5.0),
        rain = null,
        clouds = CloudsDto(all = 40),
        dt = 1625097600L,
        sys = SysDto(
            type = 1,
            id = 1414,
            country = "GB",
            sunrise = 1625024660,
            sunset = 1625084531
        ),
        timezone = 3600,
        id = 2643743,
        name = name,
        cod = 200
    )

    @Test
    fun `toDomain transforms DTO correctly`() {
        val description = "scattered clouds"
        val dto = createMockWeatherResponseDto(
            name = "London",
            temp = 25.0,
            description = description
        )

        val domain = dto.toDomain("id_123")

        assertEquals("id_123", domain.locationId)
        assertEquals(25.0, domain.temperature, 0.0)
        assertEquals("London", domain.name)
        assertEquals(description, domain.description)
    }
}