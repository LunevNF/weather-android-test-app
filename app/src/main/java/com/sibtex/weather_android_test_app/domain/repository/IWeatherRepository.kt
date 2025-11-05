package com.sibtex.weather_android_test_app.domain.repository

import com.sibtex.weather_android_test_app.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    fun getWeatherData(lat: Double, lon: Double): Flow<Result<WeatherData>>
}

