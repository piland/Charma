import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.gson.JsonParser
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Polyline
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.maps.android.compose.widgets.ScaleBar
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data classes for FavoritePlace and SelectedPlaceData
data class FavoritePlace(
    val id: String,
    val name: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double
)

data class SelectedPlaceData(
    val id: String,
    val name: String?,
    val address: String?,
    val latLng: LatLng
)

@Composable
fun MainContent(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf(LatLng(35.3076, -80.7351)) }

    if (!Places.isInitialized()) {
        Places.initialize(context.applicationContext, context.getGoogleApiKey())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getUserLocation(fusedLocationClient) { location ->
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 18f)
    }

    val compassRotation by remember { derivedStateOf { cameraPositionState.position.bearing } }
    var showEmergencyOptions by remember { mutableStateOf(false) }
    var selectedPlaceData by remember { mutableStateOf<SelectedPlaceData?>(null) }
    val favoritesList = remember { mutableStateListOf<FavoritePlace>() }
    var showFavorites by remember { mutableStateOf(false) }
    var routePolyline by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var shouldDrawRoute by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

    val autocompleteIntent = remember(context) {
        context?.let {
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(it)
        }
    }

    LaunchedEffect(Unit) {
        val loadedFavorites = loadFavorites(context)
        favoritesList.addAll(loadedFavorites)
    }

    val autocompleteIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            intent?.let {
                val place = Autocomplete.getPlaceFromIntent(intent)
                selectedPlaceData = SelectedPlaceData(
                    id = place.id ?: "",
                    name = place.name,
                    address = place.address,
                    latLng = place.latLng ?: LatLng(0.0, 0.0)
                )
                place.latLng?.let { latLng ->
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            if (shouldDrawRoute && routePolyline.isNotEmpty()) {
                Polyline(
                    points = routePolyline,
                    color = Color.Blue,
                    width = 10f
                )
            }
        }

        ScaleBar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            cameraPositionState = cameraPositionState
        )

        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.TopEnd)
                .offset(y = 70.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.compass),
                contentDescription = "Compass",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(-compassRotation)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (showEmergencyOptions) {
                EmergencyOptions()
            }

            if (selectedPlaceData != null) {
                LocationInfoCard(
                    placeData = selectedPlaceData!!,
                    isFavorited = favoritesList.any { it.id == selectedPlaceData!!.id },
                    onAddToFavorites = {
                        if (!favoritesList.any { it.id == selectedPlaceData!!.id }) {
                            val favoritePlace = FavoritePlace(
                                id = selectedPlaceData!!.id,
                                name = selectedPlaceData!!.name,
                                address = selectedPlaceData!!.address,
                                latitude = selectedPlaceData!!.latLng.latitude,
                                longitude = selectedPlaceData!!.latLng.longitude
                            )
                            favoritesList.add(favoritePlace)
                            saveFavorites(favoritesList, context)
                        }
                    },
                    onRemoveFromFavorites = {
                        favoritesList.removeAll { it.id == selectedPlaceData!!.id }
                        saveFavorites(favoritesList, context)
                    },
                    getDirections = { origin, destination ->
                        coroutineScope.launch {
                            routePolyline = getDirections(context, origin, destination)
                            shouldDrawRoute = true
                        }
                    },
                    cameraPositionState = cameraPositionState,
                    userLocation = userLocation
                )
            }

            if (showFavorites) {
                FavoritesList(favorites = favoritesList, onFavoriteClick = { favorite ->
                    selectedPlaceData = SelectedPlaceData(
                        id = favorite.id,
                        name = favorite.name,
                        address = favorite.address,
                        latLng = LatLng(favorite.latitude, favorite.longitude)
                    )
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(selectedPlaceData!!.latLng, 16f)
                    showFavorites = false
                })
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        getUserLocation(fusedLocationClient) { location ->
                            userLocation = LatLng(location.latitude, location.longitude)
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation, 18f)
                            Log.d("Camera Update", "Camera centered on: $userLocation")
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Start)
                        .background(UNCCGreen, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Recenter",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            autocompleteIntent?.let { autocompleteIntentLauncher.launch(it) }
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
                .padding(top = 80.dp, start = 16.dp)
        ) {
            WeatherWidget(latitude = 35.227085, longitude = -80.843124)
        }
    }
}

@SuppressLint("MissingPermission")
fun getUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (android.location.Location) -> Unit
) {
    fusedLocationClient.requestLocationUpdates(
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000 // Request updates every second if needed
        },
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d("Location Update", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                    onLocationResult(location)
                    // Stop updates after getting the current location if not needed continuously
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        },
        Looper.getMainLooper()
    )
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
fun LocationInfoCard(
    placeData: SelectedPlaceData,
    isFavorited: Boolean,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    getDirections: (LatLng, LatLng) -> Unit,
    cameraPositionState: CameraPositionState,
    userLocation: LatLng
) {
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
            Text(text = placeData.name ?: "Unknown Place", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = placeData.address ?: "No address available", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(
                    onClick = {
                        if (favorited) {
                            onRemoveFromFavorites()
                            favorited = false
                        } else {
                            onAddToFavorites()
                            favorited = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (favorited) Color.Gray else UNCCGreen
                    )
                ) {
                    Text(if (favorited) "Remove from Favorites" else "Add to Favorites")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation, 16f)
                        getDirections(userLocation, placeData.latLng)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
                ) {
                    Text("Get Directions")
                }
            }
        }
    }
}

@Composable
fun FavoritesList(favorites: List<FavoritePlace>, onFavoriteClick: (FavoritePlace) -> Unit) {
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
            favorites.forEach { favorite ->
                TextButton(onClick = { onFavoriteClick(favorite) }) {
                    Text(text = favorite.name ?: "Unnamed Place")
                }
                Divider()
            }
        }
    }
}

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

suspend fun getDirections(context: Context, origin: LatLng, destination: LatLng): List<LatLng> {
    val apiKey = context.getGoogleApiKey()
    val url = "https://maps.googleapis.com/maps/api/directions/json?" +
            "origin=${origin.latitude},${origin.longitude}" +
            "&destination=${destination.latitude},${destination.longitude}" +
            "&mode=walking" +
            "&key=$apiKey"
    val result = mutableListOf<LatLng>()

    withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()

                val jsonResponse = JSONObject(response)
                val routes = jsonResponse.getJSONArray("routes")
                if (routes.length() > 0) {
                    val route = routes.getJSONObject(0)
                    val legs = route.getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")

                    for (i in 0 until steps.length()) {
                        val step = steps.getJSONObject(i)
                        val polyline = step.getJSONObject("polyline").getString("points")
                        result.addAll(decodePolyline(polyline))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val latLng = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(latLng)
    }
    return poly
}

@Composable
fun MainContentPreview() {
    CharmaTheme {
        MainContent("Android")
    }
}

// Serialization functions
fun saveFavorites(favoritesList: List<FavoritePlace>, context: Context) {
    val sharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val json = gson.toJson(favoritesList)
    editor.putString("favorites_list", json)
    editor.apply()
}

fun loadFavorites(context: Context): List<FavoritePlace> {
    val sharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("favorites_list", null)
    val type = object : TypeToken<List<FavoritePlace>>() {}.type
    return if (json != null) {
        gson.fromJson(json, type)
    } else {
        emptyList()
    }
}

fun Context.getGoogleApiKey(): String {
    return try {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        appInfo.metaData.getString("com.google.android.geo.API_KEY") ?: ""
    } catch (e: Exception) {
        Log.e("API Key Error", "API Key not found in manifest")
        ""
    }
}