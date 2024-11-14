package com.example.charma.mapfeatures

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charma.ui.theme.UNCCGreen
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun WeatherWidget(latitude: Double, longitude: Double) {
    var currentTemperature by remember { mutableStateOf<Double?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(latitude, longitude) {
        isLoading = true
        errorMessage = null

        try {
            val urlString = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m&temperature_unit=fahrenheit&timezone=auto&forecast_days=1"
            val result = withContext(Dispatchers.IO) {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    errorMessage = "Error: ${connection.responseCode}"
                    null
                }
            }

            result?.let {
                val jsonObject = JsonParser.parseString(it).asJsonObject
                val current = jsonObject.getAsJsonObject("current")
                currentTemperature = current.get("temperature_2m").asDouble
            }

        } catch (e: Exception) {
            errorMessage = "Exception: ${e.localizedMessage}"
        }

        isLoading = false
    }

    Card(
        modifier = Modifier
            .width(94.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(text = errorMessage ?: "Unknown error", color = Color.Red)
            } else if (currentTemperature != null) {
                Text(
                    text = "$currentTemperature°F",
                    style = MaterialTheme.typography.bodyMedium,
                    color = UNCCGreen,
                    fontSize = 15.sp
                )
            } else {
                Text(text = "No current weather data available", color = Color.Red)
            }
        }
    }
}
