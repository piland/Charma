package com.example.charma.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.charma.R
import com.example.charma.ui.theme.NinerGold
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen

@Composable
fun RegisterPopup(onDismissRequest: () -> Unit, onCreateAccount: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            val image = painterResource(id = R.drawable.sign)

            // Create image box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QuartzWhite)
            )
            {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, //Crop image to fit screen
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter your account email."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Please enter your password")

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "By selecting Create account, you agree to our User Agreement",
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Register Account Login
                            onCreateAccount(email, password) //call the callback
                            onDismissRequest() //closes popup
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UNCCGreen
                        )
                    ) {
                        Text(text = "Create Account")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Back to Login
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NinerGold
                        )
                    )
                    {
                        Text(text = "Back to Log In")
                    }

                }
            }
        }
    }
}

@Composable
fun ForgotPassword(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        var email by remember { mutableStateOf("") }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            val image = painterResource(id = R.drawable.sign)

            // Create image box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QuartzWhite)
            )
            {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, //Crop image to fit screen
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter your account email."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Back to Login
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UNCCGreen
                        )
                    )
                    {
                        Text(text = "Send reset password email")
                    }

                }
            }
        }
    }
}


/*
package com.example.charma.popup

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.charma.DuoAuthService.DuoAuthViewModel
import com.example.charma.R
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel as viewModel1

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RegisterPopupWithDuo(
    onDismissRequest: () -> Unit,
    onCreateAccount: (String, String) -> Unit,
    duoAuthViewModel: DuoAuthViewModel = viewModel1()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by duoAuthViewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QuartzWhite)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sign),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )

                when (authState) {
                    is DuoAuthViewModel.AuthState.Idle -> {
                        // Show regular registration form
                        RegistrationForm(
                            email = email,
                            password = password,
                            onEmailChange = { email = it },
                            onPasswordChange = { password = it },
                            onCreateAccount = {
                                scope.launch {
                                    duoAuthViewModel.startEnrollment()
                                }
                            }
                        )
                    }

                    is DuoAuthViewModel.AuthState.Loading -> {
                        LoadingScreen()
                    }

                    is DuoAuthViewModel.AuthState.EnrollmentReady -> {
                        val enrollState = authState as DuoAuthViewModel.AuthState.EnrollmentReady
                        EnrollmentScreen(
                            activationCode = enrollState.activationCode,
                            barcode = enrollState.barcode,
                            activationUrl = enrollState.activationUrl
                        )
                    }

                    is DuoAuthViewModel.AuthState.EnrollmentComplete -> {
                        scope.launch {
                            duoAuthViewModel.authenticate()
                            onCreateAccount(email, password)
                            onDismissRequest()
                        }
                    }

                    is DuoAuthViewModel.AuthState.Error -> {
                        ErrorScreen(
                            message = (authState as DuoAuthViewModel.AuthState.Error).message,
                            onRetry = {
                                scope.launch {
                                    duoAuthViewModel.startEnrollment()
                                }
                            }
                        )
                    }

                    is DuoAuthViewModel.AuthState.AuthenticationSuccess -> {
                        onCreateAccount(email, password)
                        onDismissRequest()
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCreateAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Please enter your account email.")
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Please enter your password")
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "By selecting Create account, you agree to our User Agreement",
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
        ) {
            Text("Create Account")
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EnrollmentScreen(
    activationCode: String,
    barcode: String,
    activationUrl: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Scan QR Code or Enter Activation Code",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display QR Code
        QRCode(barcode)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Activation Code: $activationCode",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Visit $activationUrl to activate",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun QRCode(content: String) {
    val writer = remember { QRCodeWriter() }
    val bitMatrix = remember(content) {
        writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
    }

    val bitmap = remember(bitMatrix) {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
            }
        }

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }.asImageBitmap()
    }

    Image(
        bitmap = bitmap,
        contentDescription = "QR Code",
        modifier = Modifier.size(200.dp)
    )
}

@Composable
fun ForgotPassword(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        var email by remember { mutableStateOf("") }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            val image = painterResource(id = R.drawable.sign)

            // Create image box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QuartzWhite)
            )
            {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, //Crop image to fit screen
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter your account email."
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Back to Login
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UNCCGreen
                        )
                    )
                    {
                        Text(text = "Send reset password email")
                    }

                }
            }
        }
    }
}
*/
