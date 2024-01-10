package com.valonjanuzi.apiweather.data

sealed class WeatherResult {
    data class Success(val weatherResponse: WeatherResponse) : WeatherResult()
    data class Error(val errorMessage: String) : WeatherResult()
}