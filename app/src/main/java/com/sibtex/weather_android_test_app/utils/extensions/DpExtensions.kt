package com.sibtex.weather_android_test_app.utils.extensions

import android.content.res.Resources

/**
 * Конвертирует значение в dp в пиксели
 */
val Float.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Конвертирует значение в dp в пиксели
 */
val Double.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Конвертирует значение в dp в пиксели
 */
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Конвертирует значение в пикселях в dp
 */
val Float.pxToDp: Float
    get() = this / Resources.getSystem().displayMetrics.density

/**
 * Конвертирует значение в пикселях в dp
 */
val Double.pxToDp: Double
    get() = this / Resources.getSystem().displayMetrics.density

/**
 * Конвертирует значение в пикселях в dp
 */
val Int.pxToDp: Float
    get() = this / Resources.getSystem().displayMetrics.density

