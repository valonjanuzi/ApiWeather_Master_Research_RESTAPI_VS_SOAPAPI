package com.valonjanuzi.apiweather.domain.usecase

import com.valonjanuzi.apiweather.data.WeatherResult
import com.valonjanuzi.apiweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull


class FetchWeatherRestDataUseCase(
    private val repository: WeatherRepository
) {
// Fetch data
     suspend fun execute(cityName: String): WeatherResult {
        return repository.getCurrentWeatherREST(cityName)
    }

// Measure time speed
    suspend fun measureApiResponseTime(cityName: String): Long {
        val startTime = System.currentTimeMillis()
        repository.getCurrentWeatherREST(cityName)
        val endTime = System.currentTimeMillis()

        return endTime - startTime
    }

// Error Handling Test
// Test how your API handles various error scenarios, such as invalid input or server errors

    suspend fun testErrorHandling(cityName: String): Result<WeatherResult> {
        return try {
            val result = repository.getCurrentWeatherREST(cityName)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

// Concurrency Test
// Test how your API handles concurrent requests. This is a simplified version and might not fully simulate real concurrency conditions as handled by external load testing tools
    suspend fun testConcurrency(cityName: String): List<Long> {
        val responseTimes = mutableListOf<Long>()
        coroutineScope {
            repeat(10) { // Simulate 10 concurrent requests
                launch {
                    val time = measureApiResponseTime(cityName)
                    responseTimes.add(time)
                }
            }
        }
        return responseTimes
    }

// Rate Limiting Test
// Check how the API behaves when called more frequently than allowed by its rate limit.
    suspend fun testRateLimiting(cityName: String): List<WeatherResult> {
        val results = mutableListOf<WeatherResult>()
        repeat(20) { // Assuming rate limit is less than 20 calls per minute
            val result = repository.getCurrentWeatherREST(cityName)
            results.add(result)
            delay(1000) // Adjust the delay as per the rate limit
        }
        return results
    }



// Validate Data
    suspend fun testDataValidation(cityName: String): Boolean {
        val result = repository.getCurrentWeatherREST(cityName)

        if (result is WeatherResult.Success) {
            val weatherResponse = result.weatherResponse

            val isValidTemperature = weatherResponse.main.temp > -273.15 // Temperature above absolute zero
            val isValidHumidity = weatherResponse.main.humidity in 0..100 // Humidity percentage between 0 and 100
            val hasWeatherData = weatherResponse.weather.isNotEmpty() // At least one weather data entry exists
            val isValidPressure = weatherResponse.main.pressure > 0 // Pressure is a positive value
            val isValidVisibility = weatherResponse.visibility >= 0 // Visibility should not be negative

            // Combine all validation checks
            return isValidTemperature && isValidHumidity && hasWeatherData && isValidPressure && isValidVisibility
        } else {
            // If result is not a success, return false or handle error accordingly
            return false
        }
    }

    // Function to fetch data with a timeout or slow internet
    suspend fun fetchDataWithTimeout(cityName: String, timeoutMillis: Long): WeatherResult? {
        return withContext(Dispatchers.IO) {
            withTimeoutOrNull(timeoutMillis) {
                try {
                    repository.getCurrentWeatherREST(cityName)
                } catch (e: Exception) {
                    WeatherResult.Error("Failed to fetch data: ${e.message}")
                }
            }
        }
    }



}