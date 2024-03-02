package com.antonio.weathertask.ui.weather

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.antonio.weathertask.R
import com.antonio.weathertask.data.WeatherState
import com.antonio.weathertask.ui.theme.WeatherTaskTheme
import kotlin.math.roundToInt

// Configuration and utilities specific to WeatherScreen
object WeatherScreenConfig {
    const val DEFAULT_CITY = "Search city"

    fun formatTemperature(temperature: Double): String {
        val roundedTemp = temperature.roundToInt()
        return "$roundedTemp°C"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    WeatherTaskTheme {
        val context = LocalContext.current
        val weatherState by viewModel.weatherState.collectAsState()
        var searchText by remember { mutableStateOf(WeatherScreenConfig.DEFAULT_CITY) }
        // Function to update searchText, because it's in another compose function
        val updateSearchText: (String) -> Unit = { newText ->
            searchText = newText
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Weather App") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            content = { paddingValues ->
                Surface(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LocationPermission(context) {
                            viewModel.fetchCurrentLocation(context) { location ->
                                viewModel.fetchWeather(location.latitude, location.longitude, "968d9a866e493c7c0746b657a2bf2fcc")
                            }
                        }
                        SearchBar(searchText, onSearchChanged = { searchText = it }) {
                            viewModel.fetchWeatherByName(searchText)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        WeatherContent(weatherState, updateSearchText)
                    }
                }
            }
        )
    }
}

@Composable
fun LocationPermission(context: Context, onPermissionGranted: () -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                onPermissionGranted()
            }
        }
    )

    LaunchedEffect(key1 = true) {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onPermissionGranted()
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchChanged: (String) -> Unit, onSearch: () -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchChanged,
        label = { Text("Enter city name") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Filled.Search, "search")
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun WeatherContent(weatherState: WeatherState, updateSearchText: (String) -> Unit) {
    if (weatherState.isLoading) {
        CircularProgressIndicator()
    } else if (weatherState.weatherInfo != null) {
        updateSearchText(weatherState.weatherInfo.cityName)
        // Display weather information
        Text(text = "Weather in ${weatherState.weatherInfo.cityName}", style = MaterialTheme.typography.headlineMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            // This is a placeholder, replace with actual weather condition icons
            Image(painter = painterResource(R.drawable.outline_cloud_24), contentDescription = "Weather Icon", Modifier.size(48.dp))
            Column {
                Text(text = WeatherScreenConfig.formatTemperature(weatherState.weatherInfo.temperature), style = MaterialTheme.typography.displaySmall)
                Text(text = "Description: ${weatherState.weatherInfo.weatherDescription}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    } else if (weatherState.error != null) {
        Text("Error fetching weather: ${weatherState.error}", color = Color.Red)
    }
}