package com.example.charma.popup

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.Request
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.charma.ui.theme.QuartzWhite
import com.example.charma.ui.theme.UNCCGreen
import com.example.charma.ui.theme.bahnschriftFamily


@Composable
fun AccountSettingsPopup(
    onDismissRequest: () -> Unit,
    resetEmail: (String) -> Unit,
    resetPassword: (String) -> Unit,
    reportIssue: (String) -> Unit
) {
    // States for input fields
    val newEmail = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val issueDescription = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text("Account Settings") },
        text = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                // Input field for resetting email
                /*TextField(
                    value = newEmail.value,
                    onValueChange = { newEmail.value = it },
                    placeholder = { Text("Enter new email") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )*/
                Button(
                    onClick = { resetEmail(newEmail.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen,
                        contentColor = QuartzWhite
                    )
                ) {
                    Text("Reset Email", fontFamily = bahnschriftFamily)
                }

                // Input field for resetting password
                /*TextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    placeholder = { Text("Enter new password") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )*/
                Button(
                    onClick = { resetPassword(newPassword.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen,
                        contentColor = QuartzWhite
                )
                ) {
                    Text("Reset Password", fontFamily = bahnschriftFamily)
                }

//                 Input field for reporting an issue
                TextField(
                    value = issueDescription.value,
                    onValueChange = { issueDescription.value = it },
                    placeholder = { Text("Describe the issue", fontFamily = bahnschriftFamily) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Button(
                    onClick = { reportIssue(issueDescription.value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UNCCGreen,
                        contentColor = QuartzWhite
                    )
                ) {
                    Text("Report an Issue", fontFamily = bahnschriftFamily)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = UNCCGreen,
                    contentColor = QuartzWhite
            )
            ) {
                Text("Close", fontFamily = bahnschriftFamily)
            }
        }
    )
}
