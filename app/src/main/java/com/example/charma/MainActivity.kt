
package com.example.charma

import MainContent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.charma.authenticationmanager.AuthenticationManager
import com.example.charma.popup.RegisterPopup
import com.example.charma.splashscreen.LoginScreen
import com.example.charma.ui.theme.CharmaTheme
import com.example.charma.popup.AccountSettingsPopup
import okhttp3.internal.ignoreIoExceptions

class MainActivity : ComponentActivity() {
    //Firebase Authentication and Database references
    internal val authenticationManager = AuthenticationManager(this)
    internal var isLoggedIn by mutableStateOf(false)
    var showAccountSettingsPopup by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CharmaTheme {
                //var isLoggedIn by remember { mutableStateOf(false) }
                var showRegisterPopup by remember { mutableStateOf(false) }
                //var showAccountSettingsPopup by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if(isLoggedIn){
                        //This is where the main content is displayed(under the compass, top-right area)
                        Box(Modifier.fillMaxSize()){
                            MainContent(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )

                            //The "Account Settings" button
                            IconButton(
                                onClick = {showAccountSettingsPopup = true}, //show popup on click
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 150.dp, end = 16.dp) //Adjust padding as needed
                            ){
                                Icon(painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription = "Account Settings" )
                            }

                            if(showAccountSettingsPopup){
                                //Popup will show when clicked
                                AccountSettingsPopup(
                                    onDismissRequest = { showAccountSettingsPopup = false},
                                    resetEmail = {  resetEmail(toString())},
                                    resetPassword = { resetPassword(toString())}
                                )
                            }
                        }
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

    internal fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //Placeholder functions for reset email and password
    private fun resetEmail(newEmail: String) {
        /*
        val user = authenticationManager.firebaseAuth.currentUser
        user?.updateEmail(newEmail)
            ?.addOnSuccessListener {
                //Email updated successfully, now send verification
                user.sendEmailVerification()// Sends the verification email
                    .addOnSuccessListener {
                        showToast("Email updated successfully. Please verify the new email.")
                    }
                    .addOnFailureListener { exception ->
                        showToast("Failed to send verification email: ${exception.message}")
                    }
            }
            ?.addOnFailureListener { exception ->
                showToast("Failed to update email: ${exception.message}")
            }
            */

    }

    private fun resetPassword(email: String) {
        /*
        authenticationManager.firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                showToast("Password reset email sent to $email. Check your inbox")
            }
            .addOnFailureListener { exception ->
                showToast("Failed to reset email: ${exception.message}")
            }
            */

    }
}
