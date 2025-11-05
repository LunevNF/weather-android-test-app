package com.sibtex.weather_android_test_app.data.repository

import com.google.gson.Gson
import com.sibtex.weather_android_test_app.data.local.WeatherDao
import com.sibtex.weather_android_test_app.data.local.entity.WeatherEntity
import com.sibtex.weather_android_test_app.data.remote.WeatherApi
import com.sibtex.weather_android_test_app.data.remote.dto.toDomain
import com.sibtex.weather_android_test_app.domain.model.WeatherData
import com.sibtex.weather_android_test_app.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val gson: Gson,
    private val apiKey: String
) : IWeatherRepository {
    companion object {
        private val CACHE_DURATION_MS = TimeUnit.HOURS.toMillis(1)
    }

    override fun getWeatherData(lat: Double, lon: Double): Flow<Result<WeatherData>> = flow {
        try {
            val locationKey = "${lat}_$lon"
            val cached = weatherDao.getWeather(locationKey)
            
            val weatherData = if (cached != null && 
                (System.currentTimeMillis() - cached.timestamp) < CACHE_DURATION_MS) {
                // Используем кеш
                gson.fromJson(cached.weatherJson, WeatherData::class.java)
            } else {
                // Загружаем с сервера
                val response = weatherApi.getWeatherForecast(
                    apiKey = apiKey,
                    query = "$lat,$lon",
                    days = 3
                )
                val domainData = response.toDomain()
                
                // Сохраняем в кеш
                weatherDao.insertWeather(
                    WeatherEntity(
                        locationKey = locationKey,
                        weatherJson = gson.toJson(domainData),
                        timestamp = System.currentTimeMillis()
                    )
                )
                
                // Удаляем старый кеш (старше 24 часов)
                weatherDao.deleteOldCache(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24))
                
                domainData
            }
            
            emit(Result.success(weatherData))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

