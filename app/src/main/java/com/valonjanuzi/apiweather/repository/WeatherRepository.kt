package com.valonjanuzi.apiweather.repository

import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.data.WeatherResult
import retrofit2.Call


interface WeatherRepository {

    suspend fun getCurrentWeatherREST(cityName: String): WeatherResult


    suspend fun getCurrentWeatherSOAP(cityName: String): WeatherResult
}