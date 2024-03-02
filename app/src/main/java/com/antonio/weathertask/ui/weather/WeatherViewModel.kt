package com.antonio.weathertask.ui.weather

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonio.weathertask.data.WeatherInfo
import com.antonio.weathertask.data.remote.models.WeatherRequestModel
import com.antonio.weathertask.data.remote.models.WeatherResponseModel
import com.antonio.weathertask.data.WeatherState
import com.antonio.weathertask.domain.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    fun fetchWeather(lat: Double, lon: Double, appid: String) {
        _weatherState.value = _weatherState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val response =
                    weatherRepository.getCurrentWeather(WeatherRequestModel(lat, lon, appid))
                response.fold(
                    onSuccess = { weatherResponse ->
                        // Assuming WeatherResponseModel can be directly mapped to WeatherInfo
                        // You might need to convert weatherResponse to WeatherInfo here
                        val weatherInfo = WeatherInfo(
                            cityName = weatherResponse.name
                                ?: "Default", // Adjust according to actual property names
                            temperature = weatherResponse.main?.temp
                                ?: 0.0, // Adjust according to actual property names
                            weatherDescription = weatherResponse.weather?.first()?.description ?: "No description available" // Adjust according to actual property names
                        )
                        _weatherState.value =
                            WeatherState(isLoading = false, weatherInfo = weatherInfo, error = null)
                    },
                    onFailure = { exception ->
                        _weatherState.value =
                            WeatherState(isLoading = false, error = exception.message)
                    }
                )
            } catch (e: Exception) {
                _weatherState.value = WeatherState(isLoading = false, error = e.message)
            }
        }
    }

    fun fetchWeatherByName(cityName: String) {
        _weatherState.value = WeatherState(isLoading = true)
        viewModelScope.launch {
            try {
                // First, use the Geocoding API to get coordinates for the city name
                val geocodeResult = weatherRepository.getCoordinatesForCityName(cityName)
                geocodeResult.fold(
                    onSuccess = { geoResponseList ->
                        if (geoResponseList.isNotEmpty()) {
                            val firstLocation = geoResponseList.first()
                            val lat = firstLocation.lat
                            val lon = firstLocation.lon
                            // Then, use the coordinates to fetch the weather
                            val weatherResult = weatherRepository.getCurrentWeather(WeatherRequestModel(lat, lon, "968d9a866e493c7c0746b657a2bf2fcc"))
                            weatherResult.fold(
                                onSuccess = { weatherResponse ->
                                    _weatherState.value = WeatherState(weatherInfo = mapToWeatherInfo(weatherResponse), isLoading = false)
                                },
                                onFailure = { exception ->
                                    _weatherState.value = WeatherState(error = exception.message ?: "Unknown error", isLoading = false)
                                }
                            )
                        } else {
                            _weatherState.value = WeatherState(error = "Failed to find location", isLoading = false)
                        }
                    },
                    onFailure = { exception ->
                        _weatherState.value = WeatherState(error = exception.message ?: "Failed to get coordinates", isLoading = false)
                    }
                )
            } catch (e: Exception) {
                _weatherState.value = WeatherState(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    private fun mapToWeatherInfo(weatherResponse: WeatherResponseModel): WeatherInfo {
        return WeatherInfo(
            cityName = weatherResponse.name ?: "Def",
            temperature = weatherResponse.main?.temp ?: 0.0,
            weatherDescription = weatherResponse.weather?.first()?.description ?: "No description available"
        )
    }

    fun fetchCurrentLocation(context: Context, onSuccess: (Location) -> Unit) {
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        onSuccess(it)
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}