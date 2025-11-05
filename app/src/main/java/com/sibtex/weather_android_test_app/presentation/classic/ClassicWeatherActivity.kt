package com.sibtex.weather_android_test_app.presentation.classic

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.R as AppCompatR
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.sibtex.weather_android_test_app.utils.extensions.dpToPx
import com.sibtex.weather_android_test_app.presentation.shared.LocationPickerDialog
import com.sibtex.weather_android_test_app.presentation.shared.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassicWeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var weatherView: WeatherView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = true
        }

        val mainContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        
        headerContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16.dpToPx, 8.dpToPx, 16.dpToPx, 8.dpToPx)
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        val backButton = ImageButton(this).apply {
            val drawable = try {
                AppCompatResources.getDrawable(this@ClassicWeatherActivity, 
                    AppCompatR.drawable.abc_ic_ab_back_material)
            } catch (e: Exception) {
                null
            } ?: try {
                AppCompatResources.getDrawable(this@ClassicWeatherActivity, 
                    R.drawable.ic_menu_revert)
            } catch (e: Exception) {
                null
            }
            
            if (drawable != null) {
                val wrapped = DrawableCompat.wrap(drawable.mutate())
                DrawableCompat.setTint(wrapped, Color.BLACK)
                setImageDrawable(wrapped)
            }
            
            setBackgroundColor(Color.TRANSPARENT)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            contentDescription = "Назад"
            val buttonSize = 48.dpToPx
            layoutParams = LinearLayout.LayoutParams(
                buttonSize,
                buttonSize
            )
            setOnClickListener {
                finish()
            }
        }
        headerContainer!!.addView(backButton)
        
        weatherView = WeatherView(this)
        
        mainContainer.addView(headerContainer)
        mainContainer.addView(weatherView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))
        
        observeViewModel()
        
        setContentView(mainContainer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    private var headerContainer: LinearLayout? = null

    private fun observeViewModel() {
        viewModel.uiStateLiveData.observe(this) { state ->
            when {
                state.isLoading -> {
                    weatherView.showLoading()
                }
                state.error != null -> {
                    weatherView.hideLoading()
                    // Скрываем кнопку "назад", если данных нет в кеше
                    if (state.weatherData == null && headerContainer != null) {
                        headerContainer!!.visibility = View.GONE
                    }
                    showErrorDialog(state.error, state.weatherData != null) {
                        viewModel.retry()
                    }
                }
                state.weatherData != null -> {
                    weatherView.hideLoading()
                    weatherView.updateWeatherData(state.weatherData!!)
                    // Показываем кнопку "назад", если данные загружены
                    if (headerContainer != null) {
                        headerContainer!!.visibility = View.VISIBLE
                    }
                    if (headerContainer != null && headerContainer!!.childCount == 1) {
                        val location = state.weatherData!!.location
                        val cityText = TextView(this@ClassicWeatherActivity).apply {
                            text = "${location.name}, ${location.country}"
                            textSize = 20f
                            setTypeface(null, Typeface.BOLD)
                            setTextColor(Color.BLACK)
                            layoutParams = LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1f
                            ).apply {
                                marginStart = 8.dpToPx
                            }
                        }
                        headerContainer!!.addView(cityText)
                    }
                    
                    weatherView.setOnLocationClick {
                        showLocationPicker()
                    }
                }
            }
        }
    }

    private fun showLocationPicker() {
        LocationPickerDialog(
            context = this,
            initialLat = 55.7569,
            initialLon = 37.6151,
            onLocationSelected = { lat, lon ->
                viewModel.updateLocation(lat, lon)
            }
        ).show()
    }

    private fun showErrorDialog(error: String, hasCachedData: Boolean, onRetry: () -> Unit) {
        val greenColor = Color.parseColor("#4CAF50")
        
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24.dpToPx, 16.dpToPx, 24.dpToPx, 16.dpToPx)
        }
        
        val titleView = TextView(this).apply {
            text = "Ошибка"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16.dpToPx
            }
        }
        dialogView.addView(titleView)
        
        val messageView = TextView(this).apply {
            text = error
            textSize = 16f
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 24.dpToPx
            }
        }
        dialogView.addView(messageView)
        
        val buttonContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()
        
        val cancelButton = MaterialButton(this).apply {
            text = "Отмена"
            setTextColor(greenColor)
            backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            setStrokeColor(ColorStateList.valueOf(greenColor))
            strokeWidth = 2.dpToPx
            setAllCaps(false)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8.dpToPx
            }
            setOnClickListener {
                dialog.dismiss()
                finish()
            }
        }
        
        val retryButton = MaterialButton(this).apply {
            text = "Повторить"
            backgroundTintList = ColorStateList.valueOf(greenColor)
            setTextColor(Color.WHITE)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            setAllCaps(false)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                dialog.dismiss()
                onRetry()
            }
        }
        
        buttonContainer.addView(cancelButton)
        buttonContainer.addView(retryButton)
        dialogView.addView(buttonContainer)
        
        dialog.setOnCancelListener {
            finish()
        }
        
        dialog.show()
    }
}

