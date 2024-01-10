package com.valonjanuzi.apiweather.SOAP

import android.util.Log
import com.google.gson.Gson
import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.data.WeatherResult
import com.valonjanuzi.apiweather.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherSoapService {

       fun makeSoapRequest(cityName: String, apiKey: String = Constants.API_KEY): WeatherResult {

            val soapRequest = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:web="http://www.example.com/webservices">
                <soapenv:Header/>
                <soapenv:Body>
                    <web:GetCurrentWeatherData>
                        <web:cityName>$cityName</web:cityName>
                        <web:apiKey>$apiKey</web:apiKey>
                    </web:GetCurrentWeatherData>
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()

            val url = URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$apiKey")
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "text/xml")
            connection.doOutput = true

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(soapRequest)
            writer.flush()

         val responseCode = connection.responseCode

         return if (responseCode == HttpsURLConnection.HTTP_OK) {
             val inputStream = connection.inputStream
             val responseText = inputStream.bufferedReader().use { it.readText() }
             inputStream.close()
            Log.d("VALON!!", " code : ${responseText}")
             try {
                 // Attempt to parse the response
                 val weatherResponse = parseJsonResponse(responseText)
                 WeatherResult.Success(weatherResponse)
             } catch (e: Exception) {
                 // Handle parsing or other exceptions
                 WeatherResult.Error("Failed to parse response: ${e.message}")
             }
         } else {
             WeatherResult.Error("HTTP error code: $responseCode")
         }

    }

    private fun parseJsonResponse(jsonResponse: String): WeatherResponse {
        val gson = Gson()
        return gson.fromJson(jsonResponse, WeatherResponse::class.java)
    }

    suspend fun getWeatherData(cityName: String): WeatherResult {
        return withContext(Dispatchers.IO) {
             makeSoapRequest(cityName)
        }
    }

}
