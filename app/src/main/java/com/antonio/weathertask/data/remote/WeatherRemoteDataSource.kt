package com.antonio.weathertask.data.remote

import com.antonio.weathertask.data.remote.network.ApiService
import com.antonio.weathertask.data.remote.models.GeoResponseModel
import com.antonio.weathertask.data.remote.models.WeatherRequestModel
import com.antonio.weathertask.data.remote.models.WeatherResponseModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(request: WeatherRequestModel): Result<WeatherResponseModel> {
        return try {
            val response = apiService.getCurrentWeather(request.latitude, request.longitude, request.appid)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Handle HTTP errors
                Result.failure(RuntimeException("API error with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCoordinatesForCityName(cityName: String): Result<List<GeoResponseModel>> {
        return try {
            val response = apiService.getCoordinatesForCityName(cityName, apiKey = "968d9a866e493c7c0746b657a2bf2fcc")
            if (response.isSuccessful && response.body() != null) {
                // Assuming GeoResponseModel can be directly used with the JSON response
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get coordinates for city name"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}