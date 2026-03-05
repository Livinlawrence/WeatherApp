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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.weatherapp.BuildConfig
import com.weatherapp.R
import com.weatherapp.ui.components.CurrentWeatherSection
import com.weatherapp.ui.components.FavoriteLocationsDialog
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
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.GOOGLE_PLACES_API_KEY)
        }
    }
    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.let { resultData ->
                val place = Autocomplete.getPlaceFromIntent(resultData)
                viewModel.loadWeatherForTheSelectedLocation(
                    id = place.id, lat = place.location?.latitude, lon = place.location?.longitude
                )
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {
            viewModel.loadWeather()
        }
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadWeather()
        } else {
            permissionLauncher.launch(permission)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        WeatherContent(state)
        // Dummy Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.toggleFavoritesDialog(true) }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = Color.White
                )
            }
            if (state.showFavoritesDialog) {
                FavoriteLocationsDialog(
                    favorites = state.favorites,
                    onDismiss = { viewModel.toggleFavoritesDialog(false) },
                    onLocationSelect = { location ->
                        viewModel.loadWeatherForTheSelectedLocation(
                            id = location.id, lat = location.latitude, lon = location.longitude
                        )
                        viewModel.toggleFavoritesDialog(false)
                    },
                    onDelete = { id -> viewModel.removeFavorite(id) })
            }

            IconButton(onClick = {
                val fields = listOf(
                    Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION
                )
                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields
                ).build(context)
                autocompleteLauncher.launch(intent)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Location",
                    tint = Color.White
                )
            }
        }

        //FAB for favorite action
        FloatingActionButton(
            onClick = {
                viewModel.toggleFavorite()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = if (state.isFavorite) Color.White else Color(0xFF47AB2F),
            contentColor = if (state.isFavorite) Color.Red else Color.White
        ) {
            Icon(
                imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites"
            )
        }
    }
}

@Composable
fun WeatherContent(
    state: WeatherUiState
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
            state.weather?.let { weather ->
                CurrentWeatherSection(weather)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.5f) // Semi-transparent white looks cleaner
            )
            ForecastContent(state.condition, state.forecast)
        }
    }
}

@Composable
fun ForecastContent(condition: WeatherCondition, forecast: List<ForecastUi>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(weatherBackgroundColor(condition))
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp
            )
    ) {
        forecast.forEach {
            ForecastRow(it)
        }
    }
}

fun weatherBackground(condition: WeatherCondition): Int {
    return when (condition) {
        WeatherCondition.SUNNY -> R.drawable.forest_sunny
        WeatherCondition.CLOUDY -> R.drawable.forest_cloudy
        WeatherCondition.RAINY -> R.drawable.forest_rainy
    }
}

fun weatherBackgroundColor(condition: WeatherCondition): Color {
    return when (condition) {
        WeatherCondition.SUNNY -> Color(0xFF47AB2F)  // Sunny Green
        WeatherCondition.CLOUDY -> Color(0xFF54717A) // Cloudy Grey
        WeatherCondition.RAINY -> Color(0xFF57575D)  // Rainy Dark Grey
    }
}
