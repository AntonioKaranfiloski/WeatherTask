package com.antonio.weathertask.data

import com.antonio.weathertask.data.WeatherInfo

data class WeatherState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val error: String? = null
)
