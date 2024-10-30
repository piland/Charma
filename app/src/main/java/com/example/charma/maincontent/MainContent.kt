import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charma.R
import com.example.charma.ui.theme.CharmaTheme
import com.example.charma.ui.theme.UNCCGreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.maps.android.compose.widgets.ScaleBar

@Composable
fun MainContent(name: String, modifier: Modifier = Modifier) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(35.3076, -80.7351), 18f)
    }

    val context = LocalContext.current
    var showEmergencyOptions by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val favoritesList = remember { mutableStateListOf<Place>() }
    var showFavorites by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            intent?.let {
                val place = Autocomplete.getPlaceFromIntent(intent)
                selectedPlace = place
                place.latLng?.let { latLng ->
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        ScaleBar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            cameraPositionState = cameraPositionState
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (showEmergencyOptions) {
                EmergencyOptions()
            }

            if (selectedPlace != null) {
                LocationInfoCard(
                    place = selectedPlace!!,
                    isFavorited = favoritesList.contains(selectedPlace),
                    onAddToFavorites = {
                        if (!favoritesList.contains(selectedPlace)) {
                            favoritesList.add(selectedPlace!!)
                        }
                    }
                )
            }

            if (showFavorites) {
                FavoritesList(favorites = favoritesList, onFavoriteClick = { place ->
                    selectedPlace = place
                    place.latLng?.let { latLng ->
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                    }
                    showFavorites = false
                })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
                        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(context)
                        launcher.launch(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen
                    )
                ) {
                    Text(text = "Search")
                }

                Button(
                    onClick = { showFavorites = !showFavorites },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen
                    )
                ) {
                    Text(text = "Favorites")
                }

                Button(
                    onClick = {
                        showEmergencyOptions = !showEmergencyOptions
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(text = "Emergencies")
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.uncclogo),
            contentDescription = "UNCC Logo",
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .padding(top = 80.dp, start = 16.dp) // Position under the logo
        ) {
            WeatherWidget(latitude = 35.227085, longitude = -80.843124)
        }
    }
}


@Composable
fun EmergencyOptions() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:911")
                context.startActivity(callIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Call 911")
        }

        Button(
            onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:7046872200")
                context.startActivity(callIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
        ) {
            Text("Call Campus Police")
        }
    }
}

@Composable
fun LocationInfoCard(place: Place, isFavorited: Boolean, onAddToFavorites: () -> Unit) {
    var favorited by remember { mutableStateOf(isFavorited) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = place.name ?: "Unknown Place", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = place.address ?: "No address available", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {
                        onAddToFavorites()
                        favorited = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (favorited) Color.Gray else UNCCGreen
                    )
                ) {
                    Text(if (favorited) "Added to Favorites" else "Add to Favorites")
                }
            }
        }
    }
}

@Composable
fun FavoritesList(favorites: List<Place>, onFavoriteClick: (Place) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Favorites", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            favorites.forEach { place ->
                TextButton(onClick = { onFavoriteClick(place) }) {
                    Text(text = place.name ?: "Unnamed Place")
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun WeatherWidget(latitude: Double, longitude: Double) {
    var currentTemperature by remember { mutableStateOf<Double?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Perform the API call asynchronously
    LaunchedEffect(latitude, longitude) {
        isLoading = true
        errorMessage = null

        try {
            // Make the API call
            val urlString = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m&temperature_unit=fahrenheit&timezone=auto&forecast_days=1"
            val result = withContext(Dispatchers.IO) {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                // Check if the response is successful
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    errorMessage = "Error: ${connection.responseCode}"
                    null
                }
            }

            // Parse the JSON response
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

    // UI to display the weather or errors
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
                // Display the current temperature
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

@Composable
fun MainContentPreview() {
    CharmaTheme {
        MainContent("Android")
    }
}
