package com.valonjanuzi.apiweather.domain.usecase

import com.valonjanuzi.apiweather.repository.WeatherRepository
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


class FetchWeatherRestAndSoapDataUseCase(
    private val repository: WeatherRepository
) {

//    //    Compare Response Sizes
//    private fun calculateResponseSize(response: Any): Int {
//        // Convert the response object to a string representation
//        // The way you convert this may vary based on the structure of your response object
//        val responseString = response.toString()
//
//        // Calculate the size of the string in bytes
//        return responseString.toByteArray(Charsets.UTF_8).size
//    }
//
//    suspend fun compareResponseSizes(cityName: String): Pair<Int, Int> {
//        val restResponse = repository.getCurrentWeatherREST(cityName)
//        val soapResponse = repository.getCurrentWeatherSOAP(cityName)
//
//        val restResponseSize = calculateResponseSize(restResponse) // Implement this method
//        val soapResponseSize = calculateResponseSize(soapResponse) // Implement this method
//
//        return Pair(restResponseSize, soapResponseSize)
//    }



//    Compare Data Completeness and Structure
//Check if both APIs return the same amount of data and in the same structure. This can be important for consistency across different API types.

    @OptIn(InternalSerializationApi::class)
    private  fun isDataStructureAndContentSimilar(response1: Any, response2: Any): Boolean {
        // Serialize the response objects to JSON strings
        val json = Json { ignoreUnknownKeys = true } // configure as needed
        val jsonString1 = json.encodeToString(Any::class.serializer(), response1)
        val jsonString2 = json.encodeToString(Any::class.serializer(), response2)

        // Compare the JSON strings
        return jsonString1 == jsonString2
    }
    suspend fun compareDataStructure(cityName: String): Boolean {
        val restResponse = repository.getCurrentWeatherREST(cityName)
        val soapResponse = repository.getCurrentWeatherSOAP(cityName)

        // Implement a method to compare the structure and completeness of data
        return isDataStructureAndContentSimilar(restResponse, soapResponse)
    }



}