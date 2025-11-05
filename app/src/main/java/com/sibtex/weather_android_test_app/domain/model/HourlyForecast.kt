package com.sibtex.weather_android_test_app.domain.model

data class HourlyForecast(
    val timeEpoch: Long,
    val time: String,
    val tempC: Double,
    val tempF: Double,
    val isDay: Int,
    val condition: WeatherCondition,
    val windMph: Double,
    val windKph: Double,
    val windDegree: Int,
    val windDir: String,
    val pressureMb: Double,
    val pressureIn: Double,
    val precipMm: Double,
    val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslikeC: Double,
    val feelslikeF: Double,
    val willItRain: Int,
    val chanceOfRain: Int,
    val willItSnow: Int,
    val chanceOfSnow: Int,
    val visKm: Double,
    val visMiles: Double,
    val gustMph: Double,
    val gustKph: Double,
    val uv: Double
)

