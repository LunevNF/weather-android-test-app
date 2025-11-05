package com.sibtex.weather_android_test_app.di

import com.sibtex.weather_android_test_app.domain.repository.IWeatherRepository
import com.sibtex.weather_android_test_app.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideGetWeatherUseCase(repository: IWeatherRepository): GetWeatherUseCase {
        return GetWeatherUseCase(repository)
    }
}

