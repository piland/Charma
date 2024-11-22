package com.example.charma.popup

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp


@Composable
fun AccountSettingsPopup(
    onDismissRequest: () -> Unit,
    resetEmail: () -> Unit,
    resetPassword: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismissRequest()},
        title = { Text("Account Settings")},
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Button(onClick = { resetEmail() }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("Reset Email")
                }
                Button(onClick = { resetPassword() }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("Reset Password")
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

