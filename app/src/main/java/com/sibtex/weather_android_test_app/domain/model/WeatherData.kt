package com.sibtex.weather_android_test_app.domain.model

data class WeatherData(
    val location: Location,
    val current: CurrentWeather,
    val forecast: List<ForecastDay>
)

