package com.example.charma.DuoAuthService
// Ping Response
data class PingResponse(
    val stat: String,
    val response: PingResponseData
) {
    data class PingResponseData(
        val time: Long
    )
}

data class CheckResponse(
    val stat: String,
    val response: CheckResponseData
) {
    data class CheckResponseData(
        val time: Long
    )
}


// Enroll Response
data class EnrollResponse(
    val stat: String,
    val response: EnrollResponseData
) {
    data class EnrollResponseData(
        val activation_barcode: String,
        val activation_code: String,
        val activation_url: String,
        val expiration: Long,
        val user_id: String,
        val username: String
    )
}

// Enroll Status Response
data class EnrollStatusResponse(
    val stat: String,
    val response: String
)

// PreAuth Response
data class PreAuthResponse(
    val stat: String,
    val response: PreAuthResponseData
) {
    data class PreAuthResponseData(
        val devices: List<Device>,
        val result: String,
        val status_msg: String
    ) {
        data class Device(
            val capabilities: List<String>,
            val device: String,
            val display_name: String,
            val name: String,
            val number: String,
            val type: String
        )
    }
}


// Enroll Response (when result is "enroll")
data class EnrollResponseDetailed(
    val stat: String,
    val response: EnrollResponseDataDetailed
) {
    data class EnrollResponseDataDetailed(
        val enroll_portal_url: String,
        val result: String,
        val status_msg: String
    )
}

// Allow Response (when result is "allow" with a trusted_device_token)
data class AllowResponse(
    val stat: String,
    val response: AllowResponseData
)

data class AllowResponseData(
    val result: String,
    val status_msg: String
)

// Auth Response (success)
data class AuthResponse(
    val stat: String,
    val response: AuthResponseData
)

data class AuthResponseData(
    val result: String,
    val status: String,
    val status_msg: String,
    val trusted_device_token: String? = null,
    val txid: String? = null // For async enabled case
)

// Auth Response (failure due to factor restriction)
data class AuthFailureResponse(
    val stat: String,
    val response: AuthFailureResponseData
)

data class AuthFailureResponseData(
    val result: String,
    val status: String,
    val status_msg: String
)

// Auth Response (async case)
data class AuthAsyncResponse(
    val stat: String,
    val response: AuthAsyncResponseData
)

data class AuthAsyncResponseData(
    val txid: String
)

data class AuthStatusResponse(
    val stat: String,
    val response: AuthStatusResponseData
)

data class AuthStatusResponseData(
    val result: String,
    val status: String,
    val status_msg: String
)
