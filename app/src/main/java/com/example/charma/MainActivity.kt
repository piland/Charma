package com.example.charma

import MainContent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.charma.authenticationmanager.AuthenticationManager
import com.example.charma.popup.RegisterPopup
import com.example.charma.popup.AccountSettingsPopup
import com.example.charma.splashscreen.LoginScreen
import com.example.charma.ui.theme.CharmaTheme
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    // Firebase Authentication and Database references
    internal val authenticationManager = AuthenticationManager(this)
    internal var isLoggedIn by mutableStateOf(false)
    var showAccountSettingsPopup by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CharmaTheme {
                var showRegisterPopup by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoggedIn) {
                        // Main content is displayed here
                        Box(Modifier.fillMaxSize()) {
                            MainContent(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )

                            // The "Account Settings" button
                            IconButton(
                                onClick = { showAccountSettingsPopup = true }, // Show popup on click
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 150.dp, end = 16.dp) // Adjust padding as needed
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_settings_24),
                                    contentDescription = "Account Settings"
                                )
                            }

                            if (showAccountSettingsPopup) {
                                // Show AccountSettingsPopup when clicked
                                AccountSettingsPopup(
                                    onDismissRequest = { showAccountSettingsPopup = false },
                                    resetEmail = { resetEmail("user@example.com") }, // Example email
                                    resetPassword = { resetPassword("user@example.com") }, // Example email
                                    reportIssue = { issueDescription -> reportIssue(issueDescription) }
                                )
                            }
                        }
                    } else {
                        // Login screen logic
                        LoginScreen(
                            onLoginSuccess = { email, password ->
                                authenticationManager.loginUser(
                                    email, password,
                                    onSuccess = { isLoggedIn = true },
                                    onFailure = { message -> showToast(message) }
                                )
                            },
                            onRegisterClick = { showRegisterPopup = true },
                            createAccount = { email, password ->
                                authenticationManager.createAccount(
                                    email, password,
                                    onSuccess = { isLoggedIn = true },
                                    onFailure = { message -> showToast(message) }
                                )
                            }
                        )
                    }
                }

                if (showRegisterPopup) {
                    RegisterPopup(
                        onDismissRequest = { showRegisterPopup = false },
                        onCreateAccount = { email, password ->
                            authenticationManager.createAccount(
                                email, password,
                                onSuccess = { isLoggedIn = true },
                                onFailure = { message -> showToast(message) }
                            )
                        }
                    )
                }
            }
        }
    }

    internal fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Firebase logic to reset email
    private fun resetEmail(newEmail: String) {
        // Placeholder Firebase logic for resetting email
        showToast("Reset email function not implemented.")
    }

    // Firebase logic to reset password
    private fun resetPassword(email: String) {
        // Placeholder Firebase logic for resetting password
        showToast("Reset password function not implemented.")
    }

    // Function to report an issue to Firebase
    private fun reportIssue(issueDescription: String) {
        val database = FirebaseDatabase.getInstance()
        val issuesRef = database.getReference("issues") // Node for storing issues

        val issueId = issuesRef.push().key // Generate a unique key
        if (issueId != null) {
            issuesRef.child(issueId).setValue(issueDescription)
                .addOnSuccessListener {
                    showToast("Issue reported successfully. Thank you!")
                }
                .addOnFailureListener { exception ->
                    showToast("Failed to report issue: ${exception.message}")
                }
        } else {
            showToast("Failed to generate issue ID.")
        }
    }
}
