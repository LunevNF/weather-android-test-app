package com.sibtex.weather_android_test_app.presentation.classic

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.bumptech.glide.Glide
import com.sibtex.weather_android_test_app.domain.model.ForecastDay
import com.sibtex.weather_android_test_app.domain.model.HourlyForecast
import com.sibtex.weather_android_test_app.domain.model.WeatherData
import com.sibtex.weather_android_test_app.utils.extensions.dpToPx
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private lateinit var scrollView: ScrollView
    private lateinit var mainContainer: LinearLayout
    private lateinit var currentWeatherContainer: LinearLayout
    private lateinit var hourlyContainer: LinearLayout
    private lateinit var dailyContainer: LinearLayout
    private lateinit var loadingIndicator: View
    private var onLocationClick: (() -> Unit)? = null

    init {
        setupViews()
    }

    private fun setupViews() {
        scrollView = ScrollView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }

        mainContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setPadding(32.dpToPx, 32.dpToPx, 32.dpToPx, 32.dpToPx)
        }

        currentWeatherContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 32
            }
            gravity = Gravity.CENTER_HORIZONTAL
        }

        hourlyContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 32
            }
        }

        dailyContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        loadingIndicator = createLoadingIndicator()

        mainContainer.addView(currentWeatherContainer)
        mainContainer.addView(hourlyContainer)
        mainContainer.addView(dailyContainer)
        scrollView.addView(mainContainer)

        addView(scrollView)
        addView(loadingIndicator)
    }

    private fun createLoadingIndicator(): View {
        return object : View(context) {
            private val paint = Paint().apply {
                color = Color.parseColor("#6200EE")
                style = Paint.Style.FILL
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                val centerX = width / 2f
                val centerY = height / 2f
                val radius = 40f
                canvas.drawCircle(centerX, centerY, radius, paint)
            }
        }.apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            visibility = GONE
        }
    }

    fun showLoading() {
        loadingIndicator.visibility = VISIBLE
        scrollView.visibility = GONE
    }

    fun hideLoading() {
        loadingIndicator.visibility = GONE
        scrollView.visibility = VISIBLE
    }

    fun updateWeatherData(weatherData: WeatherData) {
        currentWeatherContainer.removeAllViews()
        hourlyContainer.removeAllViews()
        dailyContainer.removeAllViews()

        // Текущая погода
        setupCurrentWeather(weatherData)

        // Почасовой прогноз
        setupHourlyForecast(weatherData.forecast.firstOrNull()?.hour ?: emptyList())

        // Дневной прогноз
        setupDailyForecast(weatherData.forecast)
    }

    private fun setupCurrentWeather(weatherData: WeatherData) {
        val current = weatherData.current

        val iconView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                200,
                200
            ).apply {
                bottomMargin = 16
            }
        }
        Glide.with(context)
            .load("https:${current.condition.icon}")
            .into(iconView)
        currentWeatherContainer.addView(iconView)

        val tempText = TextView(context).apply {
            text = "${current.tempC.toInt()}°C"
            textSize = 48f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8
            }
        }
        currentWeatherContainer.addView(tempText)

        val conditionText = TextView(context).apply {
            text = current.condition.text
            textSize = 18f
            setTextColor(Color.GRAY)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        currentWeatherContainer.addView(conditionText)

        val infoContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER
        }

        infoContainer.addView(createInfoItem("Ощущается", "${current.feelslikeC.toInt()}°C"))
        infoContainer.addView(createInfoItem("Влажность", "${current.humidity}%"))
        infoContainer.addView(createInfoItem("Ветер", "${current.windKph.toInt()} км/ч"))

        currentWeatherContainer.addView(infoContainer)
    }

    private fun createInfoItem(label: String, value: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 8
                marginStart = 8
            }
            gravity = Gravity.CENTER

            val labelView = TextView(context).apply {
                text = label
                textSize = 12f
                setTextColor(Color.GRAY)
            }
            addView(labelView)

            val valueView = TextView(context).apply {
                text = value
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            addView(valueView)
        }
    }

    private fun setupHourlyForecast(hourly: List<HourlyForecast>) {
        val title = TextView(context).apply {
            text = "Почасовой прогноз"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        hourlyContainer.addView(title)

        val hourlyScroll = HorizontalScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val hourlyLinear = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 0, 0, 0)
        }

        hourly.take(24).forEach { hour ->
            hourlyLinear.addView(createHourlyItem(hour))
        }

        hourlyScroll.addView(hourlyLinear)
        hourlyContainer.addView(hourlyScroll)
    }

    private fun createHourlyItem(hour: HourlyForecast): LinearLayout {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = try {
            timeFormat.format(Date(hour.timeEpoch * 1000))
        } catch (e: Exception) {
            hour.time.split(" ").getOrNull(1)?.take(5) ?: hour.time
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                160, // Увеличено для размещения больших иконок
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8
            }
            gravity = Gravity.CENTER
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)

            val timeView = TextView(context).apply {
                text = time
                textSize = 12f
                setTextColor(Color.BLACK)
            }
            addView(timeView)

            val iconView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    120, // Увеличено в 3 раза (было 40)
                    120  // Увеличено в 3 раза (было 40)
                ).apply {
                    topMargin = 4
                    bottomMargin = 4
                }
            }
            Glide.with(context)
                .load("https:${hour.condition.icon}")
                .into(iconView)
            addView(iconView)

            val tempView = TextView(context).apply {
                text = "${hour.tempC.toInt()}°"
                textSize = 14f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            addView(tempView)
        }
    }

    private fun setupDailyForecast(forecast: List<ForecastDay>) {
        val title = TextView(context).apply {
            text = "Прогноз на 3 дня"
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        dailyContainer.addView(title)

        forecast.forEach { day ->
            dailyContainer.addView(createDailyItem(day))
        }
        val greenColor = Color.parseColor("#4CAF50")
        val greenColorStateList = ColorStateList.valueOf(greenColor)
        val locationButton = MaterialButton(context).apply {
            text = "Выбрать местоположение"
            backgroundTintList = greenColorStateList
            setTextColor(Color.WHITE)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            setAllCaps(false)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
            }
            setOnClickListener {
                onLocationClick?.invoke()
            }
        }
        dailyContainer.addView(locationButton)
    }

    fun setOnLocationClick(listener: () -> Unit) {
        onLocationClick = listener
    }

    private fun createDailyItem(day: ForecastDay): LinearLayout {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("ru"))
        val date = try {
            dateFormat.format(Date(day.dateEpoch * 1000))
        } catch (e: Exception) {
            day.date
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
            gravity = Gravity.CENTER_VERTICAL

            val textContainer = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val dateView = TextView(context).apply {
                text = date
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            textContainer.addView(dateView)

            val conditionView = TextView(context).apply {
                text = day.day.condition.text
                textSize = 14f
                setTextColor(Color.GRAY)
            }
            textContainer.addView(conditionView)

            addView(textContainer)

            val iconView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    48,
                    48
                ).apply {
                    marginStart = 16
                    marginEnd = 16
                }
            }
            Glide.with(context)
                .load("https:${day.day.condition.icon}")
                .into(iconView)
            addView(iconView)

            val tempView = TextView(context).apply {
                text = "${day.day.mintempC.toInt()}° / ${day.day.maxtempC.toInt()}°"
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }
            addView(tempView)
        }
    }
}

