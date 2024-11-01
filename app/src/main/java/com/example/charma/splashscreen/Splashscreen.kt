
package com.example.charma.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charma.R
import com.example.charma.popup.ForgotPassword
import com.example.charma.popup.RegisterPopup
import com.example.charma.ui.theme.NinerGold
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen


// region Login
@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit, onRegisterClick: () -> Unit, createAccount: (String, String) -> Unit) {
    // Android requires all images to be placed inside one of the res/drawable directories (or its variants).
    val image = painterResource(id = R.drawable.temp)

    // Create image box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuartzWhite)
    ) {
        // Fade the background
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop, //Crop image to fit screen
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
        )

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Popup State
        var showRegisterPopup by remember { mutableStateOf(false) }
        var showForgotPasswordPopup by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Create space between content of a view and its borders
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image (
                painter = painterResource(id = R.drawable.charmalogo),
                contentDescription = "",
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Username field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )


            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    // Replace with actual login logic
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        onLoginSuccess(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UNCCGreen
                )
            ) {
                Text(text = "Log In")
            }

            // Spacer
            Spacer(modifier = Modifier.height(16.dp))

            // Register button
            Button(
                onClick = {
                    // Replace with actual login logic
                    showRegisterPopup = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NinerGold
                )
            ) {
                Text(text = "Create Account")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("Forgot your password?")
                },
                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                modifier = Modifier.clickable {
                    showForgotPasswordPopup = true // Show popup when clicked
                }
            )

        }
        //show registration popup if triggered
        if (showRegisterPopup) {
            RegisterPopup (
                onDismissRequest = { showRegisterPopup = false},
                onCreateAccount = {email, password ->
                    createAccount(email, password)
                })
        }
        if (showForgotPasswordPopup) {
            ForgotPassword (onDismissRequest = { showForgotPasswordPopup = false})
        }
    }
}

/*
package com.example.charma.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.charma.R
import com.example.charma.popup.ForgotPassword
import com.example.charma.ui.theme.NinerGold
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charma.DuoAuthService.ApiResult
import com.example.charma.DuoAuthService.DuoAuthRepository
import com.example.charma.popup.RegisterPopupWithDuo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State class to handle authentication UI state
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    data class Success(val userId: String) : AuthState()
    data class DuoAuthRequired(val txId: String) : AuthState()
}

class AuthViewModel(
    private val repository: DuoAuthRepository = DuoAuthRepository()
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // First, check if DuoAuth is available
                when (val pingResult = repository.ping()) {
                    is ApiResult.Success -> {
                        // Proceed with auth flow
                        when (val authResult = repository.preAuth()) {
                            is ApiResult.Success -> {
                                when (authResult.data.response.result) {
                                    "allow" -> {
                                        // No 2FA needed, proceed with login
                                        proceedWithLogin(email)
                                    }
                                    "auth" -> {
                                        // Initiate 2FA
                                        initiateDuoAuth(email)
                                    }
                                    else -> {
                                        _authState.value = AuthState.Error("Invalid auth state")
                                    }
                                }
                            }
                            is ApiResult.Error -> {
                                _authState.value = AuthState.Error(authResult.message)
                            }
                            else -> {
                                _authState.value = AuthState.Error("Unknown error occurred")
                            }
                        }
                    }
                    is ApiResult.Error -> {
                        _authState.value = AuthState.Error("DuoAuth service unavailable")
                    }
                    else -> {
                        _authState.value = AuthState.Error("Unknown error occurred")
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private suspend fun initiateDuoAuth(email: String) {
        when (val authResult = repository.auth(async = true)) {
            is ApiResult.Success -> {
                authResult.data.response.txid?.let { txId ->
                    _authState.value = AuthState.DuoAuthRequired(txId)
                } ?: run {
                    _authState.value = AuthState.Error("Invalid authentication response")
                }
            }
            is ApiResult.Error -> {
                _authState.value = AuthState.Error(authResult.message)
            }
            else -> {
                _authState.value = AuthState.Error("Unknown error occurred")
            }
        }
    }

    fun checkDuoAuthStatus(txId: String) {
        viewModelScope.launch {
            when (val statusResult = repository.authStatus()) {
                is ApiResult.Success -> {
                    when (statusResult.data.response.result) {
                        "allow" -> {
                            _authState.value = AuthState.Success(txId)
                        }
                        "deny" -> {
                            _authState.value = AuthState.Error("Authentication denied")
                        }
                        "waiting" -> {
                            // Still waiting for user action
                            // You might want to implement polling here
                        }
                        else -> {
                            _authState.value = AuthState.Error("Invalid status")
                        }
                    }
                }
                is ApiResult.Error -> {
                    _authState.value = AuthState.Error(statusResult.message)
                }
                else -> {
                    _authState.value = AuthState.Error("Unknown error occurred")
                }
            }
        }
    }

    private suspend fun proceedWithLogin(email: String) {
        // Here you would typically:
        // 1. Validate credentials with your backend
        // 2. Store auth token
        // 3. Navigate to main screen
        _authState.value = AuthState.Success(email)
    }
}


// Add DuoAuth dialog component
@Composable
private fun DuoAuthDialog(
    txId: String,
    onDismiss: () -> Unit,
    onAuthComplete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Two-Factor Authentication Required",
                    style = MaterialTheme.typography.titleLarge
                )

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Please approve the authentication request on your Duo Mobile app",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}


// region Login
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    createAccount: (String, String) -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var showDuoAuthDialog by remember { mutableStateOf(false) }
    var currentTxId by remember { mutableStateOf<String?>(null) }
    // Android requires all images to be placed inside one of the res/drawable directories (or its variants).
    val image = painterResource(id = R.drawable.temp)

    // Create image box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuartzWhite)
    ) {
        // Fade the background
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop, //Crop image to fit screen
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
        )

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Popup State
        var showRegisterPopup by remember { mutableStateOf(false) }
        var showForgotPasswordPopup by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        errorMessage?.let {
            LaunchedEffect(it) {
                // Clear error message after showing snackbar
                delay(3000)
                errorMessage = null
            }

            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { errorMessage = null }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(it)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Create space between content of a view and its borders
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image (
                painter = painterResource(id = R.drawable.charmalogo),
                contentDescription = "",
                modifier = Modifier.size(200.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Username field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )


            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    // Replace with actual login logic
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(email, password)
                    } else {
                        errorMessage = "Please enter both email and password"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UNCCGreen
                ),
                enabled = authState !is AuthState.Loading
            ) {
                when (authState) {
                    is AuthState.Loading -> {
                        CircularProgressIndicator(color = Color.White,
                            modifier = Modifier.size(24.dp))
                    }
                    else -> {
                        Text(text = "Log In")
                    }
                }
            }

            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.Success -> {
                        val userId = (authState as AuthState.Success).userId
                        onLoginSuccess(email, userId)
                    }
                    is AuthState.DuoAuthRequired -> {
                        val txId = (authState as AuthState.DuoAuthRequired).txId
                        // Show Duo Auth UI or handle accordingly
                        currentTxId = txId
                        showDuoAuthDialog = true

                        while (showDuoAuthDialog) {
                            viewModel.checkDuoAuthStatus(txId)
                            delay(2000)
                        }
                    }
                    is AuthState.Error -> {
                        // Show error message
                        val errorMessage = (authState as AuthState.Error).message
                    }
                    else -> {} // Handle other states
                }
            }

            // Spacer
            Spacer(modifier = Modifier.height(16.dp))

            // Register button
            Button(
                onClick = {
                    // Replace with actual login logic
                    showRegisterPopup = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NinerGold
                )
            ) {
                Text(text = "Create Account")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("Forgot your password?")
                },
                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                modifier = Modifier.clickable {
                    showForgotPasswordPopup = true // Show popup when clicked
                }
            )

        }
        //show registration popup if triggered
        if (showDuoAuthDialog && currentTxId != null) {
            DuoAuthDialog(
                txId = currentTxId!!,
                onDismiss = {
                    showDuoAuthDialog = false
                    currentTxId = null
                },
                onAuthComplete = {
                    showDuoAuthDialog = false
                    currentTxId = null
                }
            )
        }
        if (showRegisterPopup) {
            RegisterPopupWithDuo (
                onDismissRequest = { showRegisterPopup = false},
                onCreateAccount = {email, password ->
                    createAccount(email, password)
                })
        }
        if (showForgotPasswordPopup) {
            ForgotPassword (onDismissRequest = { showForgotPasswordPopup = false})
        }
    }
}*/
