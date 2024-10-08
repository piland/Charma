package com.example.charma.maincontent

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.charma.R
import com.example.charma.ui.theme.CharmaTheme
import com.example.charma.ui.theme.UNCCGreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MainContent(name: String, modifier: Modifier = Modifier)
{

    // Set initial camera position for Google Maps
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(35.3076, -80.7351), 16f)
    }

    // Context to be used for dialing
    val context = LocalContext.current

    // State to toggle emergency options visibility
    var showEmergencyOptions by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize())
    {
        // Google Map Composable
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        // UI elements overlaid on top of the map
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        )
        {
            Spacer(modifier = Modifier.weight(1f))

            // Emergency Options
            if (showEmergencyOptions)
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    // 911 Button
                    Button(
                        onClick =
                        {
                            // Intent to call 911
                            val callIntent = Intent(Intent.ACTION_DIAL)
                            callIntent.data = Uri.parse("tel:911")
                            context.startActivity(callIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),

                        // Set to red for 911
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    )
                    {
                        Text(text = "Call 911")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campus Police Button
                    Button(
                        onClick =
                        {
                            // Intent to call campus police
                            val callIntent = Intent(Intent.ACTION_DIAL)
                            callIntent.data = Uri.parse("tel:7046872200")
                            context.startActivity(callIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UNCCGreen
                        )
                    )
                    {
                        Text(text = "Call Campus Police")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Main action buttons (Search, Favorites, Emergencies)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* TODO: Handle Search action */ },
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

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    CharmaTheme {
        MainContent("Android")
    }
}
