package com.antonio.weathertask.data.repository

import com.antonio.weathertask.data.remote.WeatherRemoteDataSource
import com.antonio.weathertask.data.remote.models.GeoResponseModel
import com.antonio.weathertask.data.remote.models.WeatherRequestModel
import com.antonio.weathertask.data.remote.models.WeatherResponseModel
import com.antonio.weathertask.domain.WeatherRepository

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteDataSource
): WeatherRepository {

    override suspend fun getCurrentWeather(weatherRequest: WeatherRequestModel): Result<WeatherResponseModel> {
        return remote.getCurrentWeather(weatherRequest)
    }

    override suspend fun getCoordinatesForCityName(cityName: String): Result<List<GeoResponseModel>> {
        return remote.getCoordinatesForCityName(cityName)
    }

}