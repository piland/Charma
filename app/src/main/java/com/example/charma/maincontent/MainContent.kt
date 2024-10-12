import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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


@Composable
fun MainContent(name: String, modifier: Modifier = Modifier) {
    // Set initial camera position for Google Maps
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(35.3076, -80.7351), 16f)
    }

    // Context to be used for dialing and launching intents
    val context = LocalContext.current

    // State to toggle emergency options visibility
    var showEmergencyOptions by remember { mutableStateOf(false) }

    // Google Places Autocomplete launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            intent?.let {
                val place = Autocomplete.getPlaceFromIntent(intent)
                val latLng = place.latLng
                latLng?.let {
                    // Update map camera to the selected place
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map Composable
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        // UI elements overlaid on top of the map
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Conditionally show emergency options if showEmergencyOptions is true
            if (showEmergencyOptions) {
                EmergencyOptions()
            }

            // Emergency Options and main action buttons (Search, Favorites, Emergencies)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Trigger the autocomplete intent for searching places
                        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .build(context)
                        launcher.launch(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen // Set to UNCC Green
                    )
                ) {
                    Text(text = "Search")
                }

                Button(
                    onClick = { /* TODO: Handle Favorites action */ },
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
                        // Toggle visibility of emergency options
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

        // UNCC Logo at the top-left corner
        Image(
            painter = painterResource(id = R.drawable.uncclogo),
            contentDescription = "UNCC Logo",
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp)
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun EmergencyOptions() {
    // UI for emergency options
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // TODO: Handle 911 action
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
                // TODO: Handle Campus Police action
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
fun MainContentPreview() {
    CharmaTheme {
        MainContent("Android")
    }
}
