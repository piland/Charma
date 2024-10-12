package com.example.charma

import MainContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.charma.splashscreen.LoginScreen
import com.example.charma.ui.theme.CharmaTheme
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyA0bcQSyrgWDwrgHLHlrJNl3Qkl6aZ7Sl0")
        }

        enableEdgeToEdge()
        setContent {
            CharmaTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoggedIn) {
                        MainContent(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        LoginScreen(onLoginSuccess = { isLoggedIn = true })
                    }
                }
            }
        }
    }
}

