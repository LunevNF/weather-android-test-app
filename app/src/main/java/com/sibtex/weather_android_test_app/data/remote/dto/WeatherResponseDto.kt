package com.sibtex.weather_android_test_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val location: LocationDto,
    val current: CurrentWeatherDto,
    val forecast: ForecastDto
)

data class ForecastDto(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDayDto>
)

