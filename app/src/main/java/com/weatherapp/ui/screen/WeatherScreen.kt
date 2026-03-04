package com.weatherapp.ui.screen


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.weatherapp.R
import com.weatherapp.ui.components.ForecastRow
import com.weatherapp.ui.state.ForecastUi
import com.weatherapp.ui.state.WeatherCondition
import com.weatherapp.ui.state.WeatherUiState
import com.weatherapp.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                viewModel.loadWeather()
            }
        }

    LaunchedEffect(Unit) {

        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            viewModel.loadWeather()

        } else {

            permissionLauncher.launch(permission)
        }
    }

    WeatherContent(state)
}

@Composable
fun WeatherContent(
    state: WeatherUiState
) {

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Blue)
        }
        return
    }

    val background = weatherBackground(state.condition)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            CurrentWeatherSection(state)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.5f) // Semi-transparent white looks cleaner
            )
            ForecastSection(state.condition, state.forecast)
        }
    }
}

@Composable
fun CurrentWeatherSection(state: WeatherUiState) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.weather?.let { currentWeather ->
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
}


@Composable
fun TempColumn(label: String, temp: Int) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "$temp°",
            color = Color.White,
            fontSize = 18.sp
        )

        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}


@Composable
fun ForecastSection(condition: WeatherCondition, forecast: List<ForecastUi>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .background(weatherBackgroundColor(condition))
            .padding(16.dp)
    ) {
        forecast.take(5).forEach {
            ForecastRow(it)
        }
    }
}


@Composable
fun weatherBackground(condition: WeatherCondition): Int {
    return when (condition) {
        WeatherCondition.SUNNY -> R.drawable.forest_sunny
        WeatherCondition.CLOUDY -> R.drawable.forest_cloudy
        WeatherCondition.RAINY -> R.drawable.forest_rainy
    }
}


@Composable // You can actually remove @Composable if you aren't using resources
fun weatherBackgroundColor(condition: WeatherCondition): Color {
    return when (condition) {
        WeatherCondition.SUNNY -> Color(0xFF47AB2F)  // Sunny Green
        WeatherCondition.CLOUDY -> Color(0xFF54717A) // Cloudy Grey/Blue
        WeatherCondition.RAINY -> Color(0xFF57575D)  // Rainy Dark Grey
    }
}
