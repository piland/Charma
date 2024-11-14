
package com.example.charma

import MainContent
import android.os.Bundle
import android.widget.Toast
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
import com.example.charma.authenticationmanager.AuthenticationManager
import com.example.charma.popup.RegisterPopup
import com.example.charma.splashscreen.LoginScreen
import com.example.charma.ui.theme.CharmaTheme

class MainActivity : ComponentActivity() {
    //Firebase Authentication and Database references
    private val authenticationManager = AuthenticationManager(this)
    private var isLoggedIn by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CharmaTheme {
                //var isLoggedIn by remember { mutableStateOf(false) }
                var showRegisterPopup by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoggedIn) {
                        MainContent(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        //Pass loginUser to LoginScreen
                        LoginScreen(
                            onLoginSuccess = { email, password -> authenticationManager.loginUser(email, password,
                                onSuccess = { isLoggedIn = true },
                                onFailure = { message -> showToast(message) }) },
                            onRegisterClick = { showRegisterPopup = true },
                            createAccount = { email, password ->
                                authenticationManager.createAccount(
                                    email,
                                    password,
                                    onSuccess = { isLoggedIn = true },
                                    onFailure = { message -> showToast(message) }
                                )
                            } //Pass the createAccount function
                        )

                    }
                }
                if (showRegisterPopup) {
                    RegisterPopup(onDismissRequest = { showRegisterPopup = false },
                        onCreateAccount = { email, password ->
                            authenticationManager.createAccount(email, password,
                                onSuccess = { isLoggedIn = true },
                                onFailure = { message -> showToast(message) })
                        })
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
