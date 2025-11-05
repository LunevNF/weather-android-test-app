package com.sibtex.weather_android_test_app.presentation.main

import android.R
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.sibtex.weather_android_test_app.presentation.classic.ClassicWeatherActivity
import com.sibtex.weather_android_test_app.presentation.compose.ComposeWeatherActivity
import com.sibtex.weather_android_test_app.utils.extensions.dpToPx

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = true
        }
        
        val container = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(64.dpToPx, 64.dpToPx, 64.dpToPx, 64.dpToPx)
        }

        val greenColor = "#4CAF50".toColorInt()
        val greenColorStateList = ColorStateList.valueOf(greenColor)
        
        val composeButton = MaterialButton(this).apply {
            text = "Компоуз"
            backgroundTintList = greenColorStateList
            setTextColor(Color.WHITE)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                bottomMargin = 32
            }
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ComposeWeatherActivity::class.java))
            }
        }

        val classicButton = MaterialButton(this).apply {
            text = "Классика"
            backgroundTintList = greenColorStateList
            setTextColor(Color.WHITE)
            setPadding(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                topMargin = 32
            }
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ClassicWeatherActivity::class.java))
            }
        }

        val buttonContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        buttonContainer.addView(composeButton)
        buttonContainer.addView(classicButton)
        container.addView(buttonContainer)

        setContentView(container)

        val rootView = findViewById<View>(R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

