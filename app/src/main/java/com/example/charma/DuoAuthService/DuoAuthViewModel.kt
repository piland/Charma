package com.example.charma.DuoAuthService

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DuoAuthViewModel(
    private val repository: DuoAuthRepository = DuoAuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
        data class EnrollmentReady(
            val activationCode: String,
            val activationUrl: String,
            val barcode: String
        ) : AuthState()
        object EnrollmentComplete : AuthState()
        data class AuthenticationSuccess(val token: String?) : AuthState()
    }

    suspend fun startEnrollment() {
        _authState.value = AuthState.Loading

        when (val result = repository.enroll()) {
            is ApiResult.Success -> {
                val response = result.data.response
                _authState.value = AuthState.EnrollmentReady(
                    activationCode = response.activation_code,
                    activationUrl = response.activation_url,
                    barcode = response.activation_barcode
                )
                // Start polling for enrollment status
                pollEnrollmentStatus()
            }
            is ApiResult.Error -> {
                _authState.value = AuthState.Error(result.message)
            }
            is ApiResult.Loading -> {
                _authState.value = AuthState.Loading
            }
        }
    }

    private suspend fun pollEnrollmentStatus() {
        while (true) {
            when (val result = repository.enrollStatus()) {
                is ApiResult.Success -> {
                    if (result.data.response == "success") {
                        _authState.value = AuthState.EnrollmentComplete
                        break
                    }
                }
                is ApiResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                    break
                }
                else -> {} // Continue polling
            }
            delay(2000) // Poll every 2 seconds
        }
    }

    suspend fun authenticate(async: Boolean = false) {
        _authState.value = AuthState.Loading

        when (val result = repository.auth(async)) {
            is ApiResult.Success -> {
                val response = result.data.response
                if (response.result == "allow") {
                    _authState.value = AuthState.AuthenticationSuccess(response.trusted_device_token)
                } else if (async && response.txid != null) {
                    pollAuthStatus(response.txid)
                } else {
                    _authState.value = AuthState.Error("Authentication failed: ${response.status_msg}")
                }
            }
            is ApiResult.Error -> {
                _authState.value = AuthState.Error(result.message)
            }
            is ApiResult.Loading -> {
                _authState.value = AuthState.Loading
            }
        }
    }

    private suspend fun pollAuthStatus(txid: String) {
        while (true) {
            when (val result = repository.authStatus()) {
                is ApiResult.Success -> {
                    val response = result.data.response
                    if (response.result == "allow") {
                        _authState.value = AuthState.AuthenticationSuccess(null)
                        break
                    } else if (response.result == "deny") {
                        _authState.value = AuthState.Error("Authentication denied")
                        break
                    }
                }
                is ApiResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                    break
                }
                else -> {} // Continue polling
            }
            delay(2000) // Poll every 2 seconds
        }
    }
}