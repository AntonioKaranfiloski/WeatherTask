package com.antonio.weathertask.data.remote.network

import com.antonio.weathertask.data.remote.models.GeoResponseModel
import com.antonio.weathertask.data.remote.models.WeatherResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = "968d9a866e493c7c0746b657a2bf2fcc",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponseModel>

    @GET("geo/1.0/direct")
    suspend fun getCoordinatesForCityName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 3,
        @Query("appid") apiKey: String = "968d9a866e493c7c0746b657a2bf2fcc"
    ): Response<List<GeoResponseModel>>
}