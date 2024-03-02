package com.antonio.weathertask.data.remote.models



data class WeatherRequestModel(
    val latitude: Double,
    val longitude: Double,
    val appid: String,
    val units: String = "metric"
)