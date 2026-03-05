package com.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.domain.model.Weather
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentWeatherSection(currentWeather: Weather) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentWeather.name,
            fontSize = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "${currentWeather.temperature}°",
            fontSize = 72.sp,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
        Text(
            text = currentWeather.description.uppercase(),
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        val formattedDate = remember(currentWeather.timestamp) {
            val date = Date(currentWeather.timestamp * 1000)
            SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date)
        }

        Text(
            text = "Last checked: $formattedDate",
            fontSize = 14.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TempColumn("min", currentWeather.minTemp.toInt())
            TempColumn("current", currentWeather.temperature.toInt())
            TempColumn("max", currentWeather.maxTemp.toInt())
        }
    }
}