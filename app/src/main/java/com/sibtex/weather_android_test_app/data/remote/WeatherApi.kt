package com.sibtex.weather_android_test_app.data.remote

import com.sibtex.weather_android_test_app.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int = 3
    ): WeatherResponseDto
}

