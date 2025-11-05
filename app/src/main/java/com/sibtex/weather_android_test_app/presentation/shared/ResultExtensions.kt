package com.sibtex.weather_android_test_app.presentation.shared

inline fun <T> Result<T>.fold(
    onSuccess: (value: T) -> Unit,
    onFailure: (exception: Throwable) -> Unit
) {
    if (isSuccess) {
        onSuccess(getOrThrow())
    } else {
        onFailure(exceptionOrNull() ?: Exception("Unknown error"))
    }
}

