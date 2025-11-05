package com.sibtex.weather_android_test_app.presentation.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibtex.weather_android_test_app.domain.model.WeatherData
import com.sibtex.weather_android_test_app.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import com.sibtex.weather_android_test_app.presentation.shared.fold
import javax.inject.Inject

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherData? = null,
    val error: String? = null
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Для Classic UI используем LiveData
    private val _uiStateLiveData = MutableLiveData<WeatherUiState>()
    val uiStateLiveData: LiveData<WeatherUiState> = _uiStateLiveData

    private var currentLat: Double = 55.7569 // Москва по умолчанию
    private var currentLon: Double = 37.6151

    init {
        loadWeather(currentLat, currentLon)
    }

    fun loadWeather(lat: Double, lon: Double) {
        currentLat = lat
        currentLon = lon
        
        viewModelScope.launch {
            val loadingState = WeatherUiState(isLoading = true, error = null)
            _uiState.value = loadingState
            _uiStateLiveData.postValue(loadingState)
            
            getWeatherUseCase(lat, lon)
                .catch { exception ->
                    val errorState = WeatherUiState(
                        isLoading = false,
                        weatherData = null,
                        error = exception.message ?: "Неизвестная ошибка"
                    )
                    _uiState.value = errorState
                    _uiStateLiveData.postValue(errorState)
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { data ->
                            val successState = WeatherUiState(
                                isLoading = false,
                                weatherData = data,
                                error = null
                            )
                            _uiState.value = successState
                            _uiStateLiveData.postValue(successState)
                        },
                        onFailure = { exception ->
                            val errorState = WeatherUiState(
                                isLoading = false,
                                weatherData = null,
                                error = exception.message ?: "Неизвестная ошибка"
                            )
                            _uiState.value = errorState
                            _uiStateLiveData.postValue(errorState)
                        }
                    )
                }
        }
    }

    fun retry() {
        loadWeather(currentLat, currentLon)
    }

    fun updateLocation(lat: Double, lon: Double) {
        loadWeather(lat, lon)
    }
}

