package com.example.charma.authenticationmanager

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.charma.MainActivity

class AuthenticationManager(private val activity: MainActivity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Login function
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    user?.let {
                        if (user.isEmailVerified) {
                            onSuccess()
                        } else {
                            onFailure("Please verify your email before logging in.")
                            auth.signOut()
                        }
                    }
                } else {
                    onFailure("Authentication Failed.")
                }
            }
    }

    // Create Account function
    fun createAccount(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    user?.let {
                        sendEmailVerification(it, onSuccess, onFailure)
                    }
                } else {
                    onFailure("Account Creation Failed.")
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        user.sendEmailVerification()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Failed to send verification email.")
                }
            }
    }
}
