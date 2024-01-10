package com.valonjanuzi.apiweather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valonjanuzi.apiweather.data.Main
import com.valonjanuzi.apiweather.data.WeatherResponse
import com.valonjanuzi.apiweather.data.WeatherResult
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAppScreen(viewModel)
                }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen(viewModel: WeatherViewModel ) {
    var city by remember { mutableStateOf("") }
    val testOptions = listOf("Get Weather Data", "Response Time", "Error Handling", "Concurrency Test", "Rate Limiting Test", "Data Validation Test", "Slow Internet Test")
    var selectedOption by remember { mutableStateOf(testOptions.first()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather Test") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter City") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDialog = true }) {
                Text("Selected: ${selectedOption}")
            }

            if (showDialog) {
                OptionDialog(
                    options = testOptions,
                    onDismissRequest = { showDialog = false },
                    onOptionSelect = { option ->
                        selectedOption = option
                        showDialog = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { performSelectedTest(viewModel, city, selectedOption, true) }) {
                    Text("Get with Rest")
                }

                Button(onClick = { performSelectedTest(viewModel, city, selectedOption, false) }) {
                    Text("Get with Soap")
                }
            }

            SelectedData(viewModel = viewModel, selectedOption = selectedOption)


        }
    }
}

@Composable
fun OptionDialog(options: List<String>, onDismissRequest: () -> Unit, onOptionSelect: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "Select an Option") },
        text = {
            Column {
                options.forEach { option ->
                    TextButton(onClick = { onOptionSelect(option) }) {
                        Text(text = option)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        }
    )
}



fun performSelectedTest(
    viewModel: WeatherViewModel,
    cityName: String,
    testTitle: String,
    isRest: Boolean
) {
    when (testTitle) {
        "Get Weather Data" -> {
            if (isRest) viewModel.fetchWeatherDataRest(cityName)
            else viewModel.fetchWeatherDataSoap(cityName)
        }
        "Response Time" -> {
            if (isRest) viewModel.fetchWeatherResponseTimeRest(cityName)
            else viewModel.fetchWeatherResponseTimeSoap(cityName)
        }
        "Error Handling" -> {
            if (isRest) viewModel.testErrorHandlingRest(cityName)
            else viewModel.testErrorHandlingSoap(cityName)
        }
        "Concurrency Test" -> {
            if (isRest) viewModel.testConcurrencyRest(cityName)
            else viewModel.testConcurrencySoap(cityName)
        }
        "Rate Limiting Test" -> {
            if (isRest) viewModel.testRateLimitingRest(cityName)
            else viewModel.testRateLimitingSoap(cityName)
        }
        "Data Validation Test" -> {
            if (isRest) viewModel.testDataValidationRest(cityName)
            else viewModel.testDataValidationSoap(cityName)
        }
        "Slow Internet Test" -> {
            if (isRest) viewModel.fetchWeatherDataWithTimeoutRest(cityName)
            else viewModel.fetchWeatherDataWithTimeoutSoap(cityName)
        }
    }
}


@Composable
fun CurrentWeatherCard(main: Main) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Weather",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Temperature: ${main.temp} °C",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Feels Like: ${main.feels_like} °C",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Pressure: ${main.pressure} hPa",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Humidity: ${main.humidity}%",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun SelectedData(viewModel: WeatherViewModel, selectedOption: String ) {
    // ... other code ...

    val restData by viewModel.weatherData_Rest.observeAsState()
    val soapData by viewModel.weatherData_SOAP.observeAsState()
    val restResponseTime by viewModel.responseTime_Rest.observeAsState()
    val soapResponseTime by viewModel.responseTime_SOAP.observeAsState()
    val errorHandlingRest by viewModel.errorHandlingRest.observeAsState()
    val errorHandlingSoap by viewModel.errorHandlingSoap.observeAsState()
    val concurrencyRest by viewModel.concurrencyRest.observeAsState()
    val concurrencySoap by viewModel.concurrencySoap.observeAsState()
    val rateLimitingRest by viewModel.rateLimitingRest.observeAsState()
    val rateLimitingSoap by viewModel.rateLimitingSoap.observeAsState()
    val dataValidationRest by viewModel.dataValidationRest.observeAsState()
    val dataValidationSoap by viewModel.dataValidationSoap.observeAsState()
    val slowInternetResultRest by viewModel.slowInternetResultRest.observeAsState()
    val slowInternetResultSoap by viewModel.slowInternetResultSoap.observeAsState()


    // ... existing UI code for TextField, Buttons, and Dialog ...

    LazyColumn {
        when (selectedOption) {
            "Get Weather Data" -> {
                restData?.let { weatherData ->
                    if (weatherData is WeatherResult.Success) {
                        item { Text(text = "REST", style = MaterialTheme.typography.titleLarge) }
                        item { CurrentWeatherCard(weatherData.weatherResponse.main) }
                        item { WeatherDetails(weatherData.weatherResponse) }
                    }
                }
                soapData?.let { weatherData ->
                    if (weatherData is WeatherResult.Success) {
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                        item { Text(text = "Soap", style = MaterialTheme.typography.titleLarge) }
                        item { CurrentWeatherCard(weatherData.weatherResponse.main) }
                        item { WeatherDetails(weatherData.weatherResponse) }
                    }
                }
            }
            "Response Time" -> {
                item {
                    ResponseTimeDisplay(restResponseTime, soapResponseTime)
                }
            }
            "Error Handling" -> {
                item {
                    ErrorHandlingDisplay(errorHandlingRest, errorHandlingSoap)
                }
            }
            "Concurrency Test" -> {
                item {
                    ConcurrencyTestDisplay(concurrencyRest, concurrencySoap)
                }
            }
            "Rate Limiting Test" -> {
                item {
                    RateLimitingTestDisplay(rateLimitingRest, rateLimitingSoap)
                }
            }
            "Data Validation Test" -> {
                item {
                    DataValidationTestDisplay(dataValidationRest, dataValidationSoap)
                }
            }
            "Slow Internet Test" -> {
                item {
                    SlowInternetResultDisplay(
                        slowInternetResultRest = slowInternetResultRest,
                        slowInternetResultSoap = slowInternetResultSoap
                    )
                }
            }
            // Add cases for other options if needed
        }
    }
}


@Composable
fun WeatherDetails(weatherData: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Weather Details",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
        WeatherDetailItem("Cloudiness", "${weatherData.clouds.all}%")
        WeatherDetailItem("Visibility", "${weatherData.visibility} meters")
        WeatherDetailItem("Wind", "${weatherData.wind.speed} m/s, ${weatherData.wind.deg}°")
        WeatherDetailItem("Rain (1h)", "${weatherData.rain} mm")
        WeatherDetailItem("Sunrise", "${weatherData.sys.sunrise}")
        WeatherDetailItem("Sunset", "${weatherData.sys.sunset}")
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun ResponseTimeDisplay(restResponseTime: Long?, soapResponseTime: Long?) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text("REST API Response Time: ${restResponseTime ?: "N/A"} ms", style = MaterialTheme.typography.bodyMedium)
        Text("SOAP API Response Time: ${soapResponseTime ?: "N/A"} ms", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ErrorHandlingDisplay(
    errorHandlingRest: Result<WeatherResult>?,
    errorHandlingSoap: Result<WeatherResult>?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("REST API Error Handling", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = errorHandlingRest?.fold(
                onSuccess = { "Success: ${it}" },
                onFailure = { "Error: ${it.message}" }
            ) ?: "Waiting for response...",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("SOAP API Error Handling", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = errorHandlingSoap?.fold(
                onSuccess = { "Success: ${it}" },
                onFailure = { "Error: ${it.message}" }
            ) ?: "Waiting for response...",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ConcurrencyTestDisplay(
    concurrencyRest: List<Long>?,
    concurrencySoap: List<Long>?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("REST API Concurrency Test", style = MaterialTheme.typography.bodyMedium)
        concurrencyRest?.let { times ->
            Text("Response Times (ms):", style = MaterialTheme.typography.bodySmall)
            times.forEach { time ->
                Text("- $time", style = MaterialTheme.typography.bodySmall)
            }
        } ?: Text("Waiting for REST API response...")

        Spacer(modifier = Modifier.height(16.dp))

        Text("SOAP API Concurrency Test", style = MaterialTheme.typography.bodyMedium)
        concurrencySoap?.let { times ->
            Text("Response Times (ms):", style = MaterialTheme.typography.bodySmall)
            times.forEach { time ->
                Text("- $time", style = MaterialTheme.typography.bodySmall)
            }
        } ?: Text("Waiting for SOAP API response...")
    }
}

@Composable
fun RateLimitingTestDisplay(
    rateLimitingRest: List<WeatherResult>?,
    rateLimitingSoap: List<WeatherResult>?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("REST API Rate Limiting Test", style = MaterialTheme.typography.bodyMedium)
        rateLimitingRest?.let { results ->
            Text("Results:", style = MaterialTheme.typography.bodySmall)
            results.forEachIndexed { index, result ->
                Text("Call ${index + 1}: ${result}", style = MaterialTheme.typography.bodySmall)
            }
        } ?: Text("Waiting for REST API results...")

        Spacer(modifier = Modifier.height(16.dp))

        Text("SOAP API Rate Limiting Test", style = MaterialTheme.typography.bodyMedium)
        rateLimitingSoap?.let { results ->
            Text("Results:", style = MaterialTheme.typography.bodySmall)
            results.forEachIndexed { index, result ->
                Text("Call ${index + 1}: ${result}", style = MaterialTheme.typography.bodySmall)
            }
        } ?: Text("Waiting for SOAP API results...")
    }
}

@Composable
fun DataValidationTestDisplay(
    dataValidationRest: Boolean?,
    dataValidationSoap: Boolean?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("REST API Data Validation Test", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = when (dataValidationRest) {
                true -> "Data is valid"
                false -> "Data is invalid"
                null -> "Waiting for REST API validation results..."
            },
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("SOAP API Data Validation Test", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = when (dataValidationSoap) {
                true -> "Data is valid"
                false -> "Data is invalid"
                null -> "Waiting for SOAP API validation results..."
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SlowInternetResultDisplay(
    slowInternetResultRest: WeatherResult?,
    slowInternetResultSoap: WeatherResult?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("REST API Result", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = when (slowInternetResultRest) {
                is WeatherResult.Success -> "Success: ${slowInternetResultRest.weatherResponse.name}" // Assuming WeatherResult.Success has a data field
                is WeatherResult.Error -> "Error: ${slowInternetResultRest.errorMessage}" // Assuming WeatherResult.Error has a message field
                null -> "Slow internet or no response"
            },
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("SOAP API Result", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = when (slowInternetResultSoap) {
                is WeatherResult.Success -> "Success: ${slowInternetResultSoap.weatherResponse.name}" // Assuming similar structure as REST
                is WeatherResult.Error -> "Error: ${slowInternetResultSoap.errorMessage}"
                null -> "Slow internet or no response"
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}