package com.sibtex.weather_android_test_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey
    val locationKey: String,
    val weatherJson: String,
    val timestamp: Long
)

