package com.antonio.weathertask.di

import com.antonio.weathertask.data.remote.WeatherRemoteDataSource
import com.antonio.weathertask.data.repository.WeatherRepositoryImpl
import com.antonio.weathertask.domain.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(weatherRemoteDataSource: WeatherRemoteDataSource): WeatherRepository {
        return WeatherRepositoryImpl(weatherRemoteDataSource)
    }
}