package com.valonjanuzi.apiweather.domain.usecase

import android.util.Log
import com.google.gson.Gson
import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.data.WeatherResult
import com.valonjanuzi.apiweather.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class FetchWeatherRestDataUseCaseTest {

    private lateinit var useCase: FetchWeatherRestDataUseCase
    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        // Initialize MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Initialize the repository with MockK
        repository = mockk(relaxed = true)

        // Initialize the use case with the mocked repository
        useCase = FetchWeatherRestDataUseCase(repository)
    }

    @After
    fun tearDown() {
        // Shut down the server after test
        mockWebServer.shutdown()
    }

    @Test
    fun `fetch weather data for New York returns success`() = runBlockingTest {
        // Read the JSON response from file
        val jsonBody = this.javaClass.classLoader?.getResource("new_york_weather.json")?.readText()
        val gson = Gson()
        val mockWeatherResponse = gson.fromJson(jsonBody, WeatherResponse::class.java)

        // Create a mock successful response
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody(jsonBody ?: "")
        mockWebServer.enqueue(mockResponse)

        // Mock the repository call to return specific data
        val cityName = ""
        val expectedResult = WeatherResult.Success(mockWeatherResponse)
        coEvery { repository.getCurrentWeatherREST(cityName) } returns expectedResult

        // Execute the use case
        val result = useCase.execute(cityName)

        // Assert that the received result is the expected one
        assertEquals(expectedResult, result)
    }

    @Test
    fun fetchWeatherDataReturnsNotFoundError() = runBlockingTest {
        val cityName = ""
        val notFoundResponse = MockResponse()
        notFoundResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        notFoundResponse.setBody("")
        mockWebServer.enqueue(notFoundResponse)

        val expectedResult = WeatherResult.Error("City not found")
        coEvery { repository.getCurrentWeatherREST(cityName) } returns expectedResult

        // Execute the use case
        val result = useCase.execute(cityName)

        // Assert that the received result is the expected error
        assertEquals(expectedResult, result)
    }

}