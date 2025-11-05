package com.sibtex.weather_android_test_app.presentation.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.sibtex.weather_android_test_app.presentation.shared.LocationPickerDialog
import com.sibtex.weather_android_test_app.presentation.shared.WeatherViewModel
import com.sibtex.weather_android_test_app.ui.theme.WeatherandroidtestappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeWeatherActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()
            UpdateStatusBarColors(darkTheme)
            WeatherandroidtestappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(
                        viewModel = viewModel,
                        onBackClick = { finish() },
                        onLocationClick = { lat, lon ->
                            LocationPickerDialog(
                                context = this,
                                initialLat = lat,
                                initialLon = lon,
                                onLocationSelected = { selectedLat, selectedLon ->
                                    viewModel.updateLocation(selectedLat, selectedLon)
                                }
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UpdateStatusBarColors(darkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? ComponentActivity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view)?.apply {
                    isAppearanceLightStatusBars = !darkTheme
                }
            }
        }
    }
}

