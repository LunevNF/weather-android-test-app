package com.sibtex.weather_android_test_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sibtex.weather_android_test_app.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache WHERE locationKey = :locationKey")
    suspend fun getWeather(locationKey: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_cache WHERE timestamp < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}

