package com.valonjanuzi.apiweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valonjanuzi.apiweather.data.WeatherResult
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherRestAndSoapDataUseCase
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherRestDataUseCase
import com.valonjanuzi.apiweather.domain.usecase.FetchWeatherSoapDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchWeatherRestDataUseCase: FetchWeatherRestDataUseCase,
    private val fetchWeatherSoapDataUseCase: FetchWeatherSoapDataUseCase,
    private val fetchWeatherRestAndSoapDataUseCase: FetchWeatherRestAndSoapDataUseCase
    ) : ViewModel() {


    // Existing LiveData for REST API results
    private val _weatherData_REST = MutableLiveData<WeatherResult>()
    val weatherData_Rest: LiveData<WeatherResult> get() = _weatherData_REST

    // New LiveData for REST API response time
    private val _responseTime_REST = MutableLiveData<Long>()
    val responseTime_Rest: LiveData<Long> get() = _responseTime_REST

    // Existing LiveData for SOAP API results
    private val _weatherData_SOAP = MutableLiveData<WeatherResult>()
    val weatherData_SOAP: LiveData<WeatherResult> get() = _weatherData_SOAP

    // New LiveData for SOAP API response time
    private val _responseTime_SOAP = MutableLiveData<Long>()
    val responseTime_SOAP: LiveData<Long> get() = _responseTime_SOAP

    // LiveData for error handling test
    private val _errorHandlingRest = MutableLiveData<Result<WeatherResult>>()
    val errorHandlingRest: LiveData<Result<WeatherResult>> get() = _errorHandlingRest

    private val _errorHandlingSoap = MutableLiveData<Result<WeatherResult>>()
    val errorHandlingSoap: LiveData<Result<WeatherResult>> get() = _errorHandlingSoap

    // LiveData for concurrency test
    private val _concurrencyRest = MutableLiveData<List<Long>>()
    val concurrencyRest: LiveData<List<Long>> get() = _concurrencyRest

    private val _concurrencySoap = MutableLiveData<List<Long>>()
    val concurrencySoap: LiveData<List<Long>> get() = _concurrencySoap

    // LiveData for rate limiting test
    private val _rateLimitingRest = MutableLiveData<List<WeatherResult>>()
    val rateLimitingRest: LiveData<List<WeatherResult>> get() = _rateLimitingRest

    private val _rateLimitingSoap = MutableLiveData<List<WeatherResult>>()
    val rateLimitingSoap: LiveData<List<WeatherResult>> get() = _rateLimitingSoap

    // LiveData for data validation test
    private val _dataValidationRest = MutableLiveData<Boolean>()
    val dataValidationRest: LiveData<Boolean> get() = _dataValidationRest

    private val _dataValidationSoap = MutableLiveData<Boolean>()
    val dataValidationSoap: LiveData<Boolean> get() = _dataValidationSoap

    // LiveData to handle slow internet result
    private val _slowInternetResultRest = MutableLiveData<WeatherResult?>()
    val slowInternetResultRest: LiveData<WeatherResult?> get() = _slowInternetResultRest


    private val _slowInternetResultSoap = MutableLiveData<WeatherResult?>()
    val slowInternetResultSoap: LiveData<WeatherResult?> get() = _slowInternetResultSoap

    // Function to fetch weather data and measure response time for REST API
    fun fetchWeatherDataRest(cityName: String) {
        viewModelScope.launch {
            _weatherData_REST.value = fetchWeatherRestDataUseCase.execute(cityName)
        }
    }

    fun fetchWeatherResponseTimeRest(cityName: String) {
        viewModelScope.launch {
            _responseTime_REST.value = fetchWeatherRestDataUseCase.measureApiResponseTime(cityName)
        }
    }

    // Function to fetch weather data and measure response time for SOAP API
    fun fetchWeatherDataSoap(cityName: String) {
        viewModelScope.launch {
            _weatherData_SOAP.value = fetchWeatherSoapDataUseCase.execute(cityName)
        }
    }
    fun fetchWeatherResponseTimeSoap(cityName: String) {
        viewModelScope.launch {
            _responseTime_SOAP.value = fetchWeatherSoapDataUseCase.measureApiResponseTime(cityName)
        }
    }

    // Functions to call use case methods and update LiveData
    fun testErrorHandlingRest(cityName: String) {
        viewModelScope.launch {
            _errorHandlingRest.value = fetchWeatherRestDataUseCase.testErrorHandling(cityName)
        }
    }

    fun testErrorHandlingSoap(cityName: String) {
        viewModelScope.launch {
            _errorHandlingSoap.value = fetchWeatherSoapDataUseCase.testErrorHandling(cityName)
        }
    }

    // Function to perform concurrency test for REST API
    fun testConcurrencyRest(cityName: String) {
        viewModelScope.launch {
            _concurrencyRest.value = fetchWeatherRestDataUseCase.testConcurrency(cityName)
        }
    }

    // Function to perform concurrency test for SOAP API
    fun testConcurrencySoap(cityName: String) {
        viewModelScope.launch {
            _concurrencySoap.value = fetchWeatherSoapDataUseCase.testConcurrency(cityName)
        }
    }

    // Function to perform rate limiting test for REST API
    fun testRateLimitingRest(cityName: String) {
        viewModelScope.launch {
            _rateLimitingRest.value = fetchWeatherRestDataUseCase.testRateLimiting(cityName)
        }
    }

    // Function to perform rate limiting test for SOAP API
    fun testRateLimitingSoap(cityName: String) {
        viewModelScope.launch {
            _rateLimitingSoap.value = fetchWeatherSoapDataUseCase.testRateLimiting(cityName)
        }
    }

    // Function to perform data validation test for REST API
    fun testDataValidationRest(cityName: String) {
        viewModelScope.launch {
            _dataValidationRest.value = fetchWeatherRestDataUseCase.testDataValidation(cityName)
        }
    }

    // Function to perform data validation test for SOAP API
    fun testDataValidationSoap(cityName: String) {
        viewModelScope.launch {
            _dataValidationSoap.value = fetchWeatherSoapDataUseCase.testDataValidation(cityName)
        }
    }

    // Function to fetch weather data with a timeout REST API
    fun fetchWeatherDataWithTimeoutRest(cityName: String, timeoutMillis: Long = 10000) { // 10 seconds default
        viewModelScope.launch {
            val result = fetchWeatherRestDataUseCase.fetchDataWithTimeout(cityName, timeoutMillis)
            _slowInternetResultRest.value = result
        }
    }

    // Function to fetch weather data with a timeout SOAP API
    fun fetchWeatherDataWithTimeoutSoap(cityName: String, timeoutMillis: Long = 10000) { // 10 seconds default
        viewModelScope.launch {
            val result = fetchWeatherSoapDataUseCase.fetchDataWithTimeout(cityName, timeoutMillis)
            _slowInternetResultSoap.value = result
        }
    }


}
