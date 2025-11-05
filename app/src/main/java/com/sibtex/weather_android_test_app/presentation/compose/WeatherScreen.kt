package com.sibtex.weather_android_test_app.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sibtex.weather_android_test_app.domain.model.CurrentWeather
import com.sibtex.weather_android_test_app.domain.model.ForecastDay
import com.sibtex.weather_android_test_app.domain.model.HourlyForecast
import com.sibtex.weather_android_test_app.domain.model.Location
import com.sibtex.weather_android_test_app.domain.model.WeatherData
import com.sibtex.weather_android_test_app.presentation.shared.WeatherViewModel
import com.sibtex.weather_android_test_app.presentation.shared.fold
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onBackClick: () -> Unit = {},
    onLocationClick: (lat: Double, lon: Double) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.error != null -> {
                val errorMessage = uiState.error as String
                ErrorDialog(
                    error = errorMessage,
                    onRetry = { viewModel.retry() },
                    onSelectLocation = { 
                        val lat = 55.7569
                        val lon = 37.6151
                        onLocationClick(lat, lon)
                    }
                )
            }
            uiState.weatherData != null -> {
                WeatherContent(
                    weatherData = uiState.weatherData!!,
                    onBackClick = onBackClick,
                    onLocationClick = {
                        val lat = uiState.weatherData!!.location.lat
                        val lon = uiState.weatherData!!.location.lon
                        onLocationClick(lat, lon)
                    }
                )
            }
        }
    }
}

@Composable
fun WeatherContent(
    weatherData: WeatherData,
    onBackClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Кнопка "Назад" и название города в одной строке
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            Text(
                text = "${weatherData.location.name}, ${weatherData.location.country}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Текущая погода (без названия города, так как оно уже вверху)
        CurrentWeatherSectionWithoutLocation(
            current = weatherData.current
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Почасовая для текущего дня
        val todayForecast = weatherData.forecast.firstOrNull()
        if (todayForecast != null) {
            HourlyForecastSection(hourlyForecast = todayForecast.hour)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Прогноз на 3 дня
        ThreeDayForecastSection(forecast = weatherData.forecast)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLocationClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50) // Зеленый цвет
            )
        ) {
            Text("Выбрать местоположение")
        }
        }
    }
}

@Composable
fun CurrentWeatherSection(
    location: Location,
    current: CurrentWeather
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${location.name}, ${location.country}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Image(
            painter = rememberAsyncImagePainter("https:${current.condition.icon}"),
            contentDescription = current.condition.text,
            modifier = Modifier.size(100.dp)
        )
        
        Text(
            text = "${current.tempC.toInt()}°C",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = current.condition.text,
            fontSize = 18.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherInfoItem("Ощущается", "${current.feelslikeC.toInt()}°C")
            WeatherInfoItem("Влажность", "${current.humidity}%")
            WeatherInfoItem("Ветер", "${current.windKph.toInt()} км/ч")
        }
    }
}

@Composable
fun CurrentWeatherSectionWithoutLocation(
    current: CurrentWeather
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter("https:${current.condition.icon}"),
            contentDescription = current.condition.text,
            modifier = Modifier.size(100.dp)
        )
        
        Text(
            text = "${current.tempC.toInt()}°C",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = current.condition.text,
            fontSize = 18.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherInfoItem("Ощущается", "${current.feelslikeC.toInt()}°C")
            WeatherInfoItem("Влажность", "${current.humidity}%")
            WeatherInfoItem("Ветер", "${current.windKph.toInt()} км/ч")
        }
    }
}

@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HourlyForecastSection(
    hourlyForecast: List<HourlyForecast>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Почасовой прогноз",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(hourlyForecast.take(24)) { hour ->
                HourlyForecastItem(hour = hour)
            }
        }
    }
}

@Composable
fun HourlyForecastItem(
    hour: HourlyForecast
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = try {
        timeFormat.format(java.util.Date(hour.timeEpoch * 1000))
    } catch (e: Exception) {
        hour.time.split(" ")[1].take(5)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = time,
            fontSize = 12.sp
        )
        
        Image(
            painter = rememberAsyncImagePainter("https:${hour.condition.icon}"),
            contentDescription = hour.condition.text,
            modifier = Modifier.size(40.dp)
        )
        
        Text(
            text = "${hour.tempC.toInt()}°",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ThreeDayForecastSection(
    forecast: List<ForecastDay>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Прогноз на 3 дня",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        forecast.forEach { day ->
            DailyForecastItem(forecastDay = day)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DailyForecastItem(
    forecastDay: ForecastDay
) {
    val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.forLanguageTag("ru"))
    val date = try {
        dateFormat.format(java.util.Date(forecastDay.dateEpoch * 1000))
    } catch (e: Exception) {
        forecastDay.date
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = forecastDay.day.condition.text,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Image(
            painter = rememberAsyncImagePainter("https:${forecastDay.day.condition.icon}"),
            contentDescription = forecastDay.day.condition.text,
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = "${forecastDay.day.mintempC.toInt()}° / ${forecastDay.day.maxtempC.toInt()}°",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ErrorDialog(
    error: String,
    onRetry: () -> Unit,
    onSelectLocation: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ошибка",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Повторить")
                }
            }
        }
    }
}

