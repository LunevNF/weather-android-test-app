package com.sibtex.weather_android_test_app.di

import com.google.gson.Gson
import com.sibtex.weather_android_test_app.data.local.WeatherDao
import com.sibtex.weather_android_test_app.data.remote.WeatherApi
import com.sibtex.weather_android_test_app.data.repository.WeatherRepository
import com.sibtex.weather_android_test_app.domain.repository.IWeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApi: WeatherApi,
        weatherDao: WeatherDao,
        gson: Gson,
        apiKey: String
    ): IWeatherRepository {
        return WeatherRepository(weatherApi, weatherDao, gson, apiKey)
    }
}

