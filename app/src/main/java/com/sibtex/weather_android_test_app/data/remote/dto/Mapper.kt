package com.sibtex.weather_android_test_app.data.remote.dto

import com.sibtex.weather_android_test_app.domain.model.CurrentWeather
import com.sibtex.weather_android_test_app.domain.model.DayForecast
import com.sibtex.weather_android_test_app.domain.model.ForecastDay
import com.sibtex.weather_android_test_app.domain.model.HourlyForecast
import com.sibtex.weather_android_test_app.domain.model.Location
import com.sibtex.weather_android_test_app.domain.model.WeatherCondition
import com.sibtex.weather_android_test_app.domain.model.WeatherData

fun WeatherConditionDto.toDomain(): WeatherCondition {
    return WeatherCondition(
        text = text,
        icon = icon,
        code = code
    )
}

fun LocationDto.toDomain(): Location {
    return Location(
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tzId = tzId,
        localtimeEpoch = localtimeEpoch,
        localtime = localtime
    )
}

fun CurrentWeatherDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        lastUpdatedEpoch = lastUpdatedEpoch,
        lastUpdated = lastUpdated,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toDomain(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        visKm = visKm,
        visMiles = visMiles,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph
    )
}

fun DayForecastDto.toDomain(): DayForecast {
    return DayForecast(
        maxtempC = maxtempC,
        maxtempF = maxtempF,
        mintempC = mintempC,
        mintempF = mintempF,
        avgtempC = avgtempC,
        avgtempF = avgtempF,
        maxwindMph = maxwindMph,
        maxwindKph = maxwindKph,
        totalprecipMm = totalprecipMm,
        totalprecipIn = totalprecipIn,
        avgvisKm = avgvisKm,
        avgvisMiles = avgvisMiles,
        avghumidity = avghumidity,
        dailyWillItRain = dailyWillItRain,
        dailyChanceOfRain = dailyChanceOfRain,
        dailyWillItSnow = dailyWillItSnow,
        dailyChanceOfSnow = dailyChanceOfSnow,
        condition = condition.toDomain(),
        uv = uv
    )
}

fun HourlyForecastDto.toDomain(): HourlyForecast {
    return HourlyForecast(
        timeEpoch = timeEpoch,
        time = time,
        tempC = tempC,
        tempF = tempF,
        isDay = isDay,
        condition = condition.toDomain(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipMm = precipMm,
        precipIn = precipIn,
        humidity = humidity,
        cloud = cloud,
        feelslikeC = feelslikeC,
        feelslikeF = feelslikeF,
        willItRain = willItRain,
        chanceOfRain = chanceOfRain,
        willItSnow = willItSnow,
        chanceOfSnow = chanceOfSnow,
        visKm = visKm,
        visMiles = visMiles,
        gustMph = gustMph,
        gustKph = gustKph,
        uv = uv
    )
}

fun ForecastDayDto.toDomain(): ForecastDay {
    return ForecastDay(
        date = date,
        dateEpoch = dateEpoch,
        day = day.toDomain(),
        hour = hour.map { it.toDomain() }
    )
}

fun WeatherResponseDto.toDomain(): WeatherData {
    return WeatherData(
        location = location.toDomain(),
        current = current.toDomain(),
        forecast = forecast.forecastDay.map { it.toDomain() }
    )
}

