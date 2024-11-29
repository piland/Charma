package com.example.charma.popup

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.Request
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun AccountSettingsPopup(
    onDismissRequest: () -> Unit,
    resetEmail: (String, String, String) -> Unit,
    resetPassword: (String) -> Unit,
    reportIssue: (String) -> Unit
) {
    // States for input fields
    var currentEmail by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var resetPasswordEmail by remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text("Account Settings") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    "To reset email: Enter your current email and password to verify it is you and the new email.\n" +
                    "To reset password: Enter your email to receive reset instructions.",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = currentEmail,
                    onValueChange = {currentEmail = it},
                    label = { Text("Enter Current Email")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                TextField(
                    value = currentPassword,
                    onValueChange = {currentPassword = it},
                    label = { Text("Enter Current Password")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                TextField(
                    value = newEmail,
                    onValueChange = { newEmail = it},
                    label = { Text("Enter new Email")},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                Button(
                    onClick = { resetEmail(currentEmail, currentPassword, newEmail)},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Reset Email")
                }
                // Password reset form field
                TextField(
                    value = resetPasswordEmail,
                    onValueChange = { resetPasswordEmail = it },
                    label = { Text("Enter Email for Password") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                Button (
                    onClick = { resetPassword(resetPasswordEmail) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ){
                    Text("Reset Password")
                }
                 //Input field for reporting an issue
                TextField(
                    value = issueDescription,
                    onValueChange = { issueDescription = it },
                    placeholder = { Text("Describe the issue") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                Button(
                    onClick = { reportIssue(issueDescription) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Report an Issue")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Close")
            }
        }
    )
}
