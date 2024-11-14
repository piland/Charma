package com.example.charma.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.charma.R
import com.example.charma.ui.theme.NinerGold
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen

val UELA = """
    End User License Agreement (EULA)

    CHARMA Application
    Last Updated: 11/6/2024

    IMPORTANT – READ CAREFULLY: This End User License Agreement ("Agreement") is a legal agreement between you ("User") and CHARMA Developers ("Licensor") for the use of the CHARMA application ("Application" or "CHARMA"). By installing, accessing, or using CHARMA, you agree to be bound by the terms of this Agreement. If you do not agree to these terms, do not install, access, or use CHARMA.

    1. License Grant
    Licensor grants you a non-exclusive, non-transferable, limited license to download, install, and use CHARMA solely for your personal use, subject to the terms and conditions of this Agreement.

    2. User Eligibility
    This Application is intended solely for use by authorized users affiliated with the University of North Carolina at Charlotte (UNCC). By using CHARMA, you confirm that you are a current or past student, faculty, or staff member with a valid @charlotte.edu or @uncc.edu email address.

    3. Restrictions
    You may not:

    Modify, reverse-engineer, decompile, or disassemble any part of CHARMA.
    Reproduce, distribute, sublicense, rent, or lease CHARMA to any third party.
    Use CHARMA for any illegal or unauthorized purpose.
    Attempt to bypass or disable security mechanisms implemented in CHARMA, including but not limited to Multi-Factor Authentication (MFA).
    4. Intellectual Property
    Licensor retains all ownership, copyright, and intellectual property rights in and to CHARMA. This Agreement does not grant you any ownership rights in CHARMA or any accompanying documentation, except as explicitly stated herein.

    5. Data Collection and Privacy
    CHARMA may collect and process certain personal information as described in our [Privacy Policy]. By using CHARMA, you consent to such data collection and processing in accordance with applicable laws and the Privacy Policy.

    6. Updates and Modifications
    Licensor reserves the right to modify, update, or discontinue CHARMA at any time without prior notice. You agree that Licensor has no obligation to provide you with future versions or updates of CHARMA.

    7. Termination
    This Agreement is effective until terminated. Your rights under this Agreement will terminate immediately if you fail to comply with any term of this Agreement. Upon termination, you must cease all use of CHARMA and uninstall it from all devices.

    8. Disclaimer of Warranties
    CHARMA is provided "as is" without warranties of any kind, express or implied. Licensor does not warrant that CHARMA will be error-free, secure, or operate without interruption.

    9. Limitation of Liability
    To the maximum extent permitted by law, Licensor shall not be liable for any indirect, incidental, consequential, or punitive damages arising from your use of CHARMA, even if advised of the possibility of such damages.

    10. Governing Law
    This Agreement shall be governed by and construed in accordance with the laws of the United States of America, without regard to its conflict of laws principles.

    11. Entire Agreement
    This Agreement constitutes the entire agreement between you and Licensor with respect to CHARMA and supersedes all prior agreements, representations, or understandings, whether written or oral.

    By installing or using CHARMA, you acknowledge that you have read, understood, and agree to be bound by this EULA.
""".trimIndent()

@Composable
fun RegisterPopup(onDismissRequest: () -> Unit, onCreateAccount: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEulaAccepted by remember { mutableStateOf(false) }
    var showEulaDialog by remember { mutableStateOf(false) }

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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please enter your account email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Please enter your password",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isEulaAccepted,
                            onCheckedChange = { isEulaAccepted = it }
                        )

                        Text(
                            text = "By selecting Create account, you agree to our",
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "User Agreement",
                            color = UNCCGreen,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable { showEulaDialog = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isEulaAccepted) {
                                onCreateAccount(email, password)
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
                    ) {
                        Text(text = "Create Account")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NinerGold)
                    ) {
                        Text(text = "Back to Log In")
                    }
                }
            }
        }
    }

    if (showEulaDialog) {
        Dialog(onDismissRequest = { showEulaDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "User Agreement",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = UELA,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showEulaDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = UNCCGreen)
                    ) {
                        Text("Close")
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

@Composable
fun UserAgreementPopup(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "User Agreement",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = UELA,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}