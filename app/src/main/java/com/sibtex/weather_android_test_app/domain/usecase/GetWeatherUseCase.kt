package com.sibtex.weather_android_test_app.domain.usecase

import com.sibtex.weather_android_test_app.domain.model.WeatherData
import com.sibtex.weather_android_test_app.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(
    private val repository: IWeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Result<WeatherData>> {
        return repository.getWeatherData(lat, lon)
    }
}

