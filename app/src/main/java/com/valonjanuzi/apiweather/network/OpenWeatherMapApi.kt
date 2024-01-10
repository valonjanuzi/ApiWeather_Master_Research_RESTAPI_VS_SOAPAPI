package com.valonjanuzi.apiweather.network

import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): Response<WeatherResponse>

}