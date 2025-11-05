package com.sibtex.weather_android_test_app.domain.model

data class ForecastDay(
    val date: String,
    val dateEpoch: Long,
    val day: DayForecast,
    val hour: List<HourlyForecast>
)

