package com.antonio.weathertask.domain

import com.antonio.weathertask.data.remote.models.GeoResponseModel
import com.antonio.weathertask.data.remote.models.WeatherRequestModel
import com.antonio.weathertask.data.remote.models.WeatherResponseModel

interface WeatherRepository {

    suspend fun getCurrentWeather(weatherRequest: WeatherRequestModel): Result<WeatherResponseModel>

    suspend fun getCoordinatesForCityName(cityName: String): Result<List<GeoResponseModel>>
}