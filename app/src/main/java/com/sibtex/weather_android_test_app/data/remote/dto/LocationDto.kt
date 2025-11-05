package com.sibtex.weather_android_test_app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id")
    val tzId: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    val localtime: String
)

