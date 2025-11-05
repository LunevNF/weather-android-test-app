package com.sibtex.weather_android_test_app.presentation.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.sibtex.weather_android_test_app.presentation.classic.ClassicWeatherActivity
import com.sibtex.weather_android_test_app.presentation.compose.ComposeWeatherActivity
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val container = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(64, 64, 64, 64)
        }

        val greenColor = "#4CAF50".toColorInt()
        val greenColorStateList = ColorStateList.valueOf(greenColor)
        
        val composeButton = MaterialButton(this).apply {
            text = "Компоуз"
            backgroundTintList = greenColorStateList
            setTextColor(Color.WHITE)
            setPadding(32, 24, 32, 24)
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
            setPadding(32, 24, 32, 24)
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

        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

