package com.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.R
import com.weatherapp.ui.state.ForecastUi
import com.weatherapp.ui.state.WeatherCondition

@Composable
fun ForecastRow(item: ForecastUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = item.day,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(
                when (item.condition) {
                    WeatherCondition.SUNNY -> R.drawable.ic_clear
                    WeatherCondition.CLOUDY -> R.drawable.ic_partlysunny
                    WeatherCondition.RAINY -> R.drawable.ic_rain
                }
            ),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${item.temperature}°",
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}