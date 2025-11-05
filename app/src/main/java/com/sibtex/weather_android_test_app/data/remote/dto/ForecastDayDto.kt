package com.sibtex.weather_android_test_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: Long,
    val day: DayForecastDto,
    val hour: List<HourlyForecastDto>
)

