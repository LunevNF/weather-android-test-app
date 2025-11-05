package com.sibtex.weather_android_test_app.domain.model

data class DayForecast(
    val maxtempC: Double,
    val maxtempF: Double,
    val mintempC: Double,
    val mintempF: Double,
    val avgtempC: Double,
    val avgtempF: Double,
    val maxwindMph: Double,
    val maxwindKph: Double,
    val totalprecipMm: Double,
    val totalprecipIn: Double,
    val avgvisKm: Double,
    val avgvisMiles: Double,
    val avghumidity: Int,
    val dailyWillItRain: Int,
    val dailyChanceOfRain: Int,
    val dailyWillItSnow: Int,
    val dailyChanceOfSnow: Int,
    val condition: WeatherCondition,
    val uv: Double
)

