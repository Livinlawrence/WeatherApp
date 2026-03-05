package com.weatherapp.ui.mapper

import com.weatherapp.domain.model.Forecast
import com.weatherapp.ui.state.ForecastUi
import com.weatherapp.ui.state.WeatherCondition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Forecast.toUi(): ForecastUi {

    val date = Date(dateTime * 1000)
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    val day = formatter.format(date)
    val condition = when {
        description.contains("rain", true) -> WeatherCondition.RAINY
        description.contains("cloud", true) -> WeatherCondition.CLOUDY
        else -> WeatherCondition.SUNNY
    }

    return ForecastUi(
        day = day,
        temperature = temperature.toInt(),
        condition = condition
    )
}