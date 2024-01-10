package com.valonjanuzi.apiweather.repository

import android.util.Log
import com.valonjanuzi.apiweather.SOAP.WeatherSoapService
import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.data.WeatherResult
import com.valonjanuzi.apiweather.network.OpenWeatherMapApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val restService: OpenWeatherMapApi,
    private val soapService: WeatherSoapService

) : WeatherRepository {

    override suspend fun getCurrentWeatherREST(cityName: String): WeatherResult {
        return try {
            val response = restService.getCurrentWeatherData(cityName)
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    WeatherResult.Success(weatherResponse)
                } else {
                    WeatherResult.Error("Empty response body")
                }
            } else {
                WeatherResult.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            WeatherResult.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getCurrentWeatherSOAP(cityName: String): WeatherResult {
        return try {
            soapService.getWeatherData(cityName = cityName)
        } catch (e: Exception){
            WeatherResult.Error(e.message ?: "An error occurred 111")
        }
    }

}