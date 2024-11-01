package com.example.charma.DuoAuthService

import com.google.firebase.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Sealed class for handling API results
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int? = null, val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

// Service interface with all DuoAuth endpoints
interface DuoAuthService {
    @GET("/")
    suspend fun ping(): Response<PingResponse>

    @GET("/")
    suspend fun check(): Response<CheckResponse>

    @GET("/")
    suspend fun enroll(): Response<EnrollResponse>

    @GET("/")
    suspend fun enrollStatus(): Response<EnrollStatusResponse>

    @GET("/")
    suspend fun preAuth(): Response<PreAuthResponse>

    @GET("enroll")
    suspend fun enrollDetailed(): Response<EnrollResponseDetailed>

    @GET("allow")
    suspend fun allow(
        @Query("trusted_device_token") trustedDeviceToken: String
    ): Response<AllowResponse>

    @GET("auth")
    suspend fun auth(
        @Query("async") async: Boolean = false
    ): Response<AuthResponse>

    @GET("auth/failure")
    suspend fun authFailure(): Response<AuthFailureResponse>

    @GET("auth/async")
    suspend fun authAsync(): Response<AuthAsyncResponse>

    @GET("auth_status")
    suspend fun authStatus(): Response<AuthStatusResponse>
}

// DuoAuth API Client
object DuoAuthClient {
    private const val TIMEOUT_SECONDS = 30L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            // Add any other required headers here
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(headerInterceptor)
        .addNetworkInterceptor(loggingInterceptor)  // Changed to addNetworkInterceptor for logging
        .build()

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Service instances for each endpoint
    val pingService: DuoAuthService by lazy {
        createRetrofit("https://ping-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val checkService: DuoAuthService by lazy {
        createRetrofit("https://check-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val enrollService: DuoAuthService by lazy {
        createRetrofit("https://enroll-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val enrollStatusService: DuoAuthService by lazy {
        createRetrofit("https://enrollstatus-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val preAuthService: DuoAuthService by lazy {
        createRetrofit("https://preauth-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val authService: DuoAuthService by lazy {
        createRetrofit("https://auth-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }

    val authStatusService: DuoAuthService by lazy {
        createRetrofit("https://authstatus-nbdrl3ye6a-uc.a.run.app")
            .create(DuoAuthService::class.java)
    }
}

// Extension function to handle API responses
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(message = "Response body is null")
        } else {
            ApiResult.Error(
                code = response.code(),
                message = "API call failed with code: ${response.code()}"
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(message = e.message ?: "Unknown error occurred")
    }
}

// Repository class to handle DuoAuth operations
class DuoAuthRepository {
    suspend fun ping(): ApiResult<PingResponse> {
        return safeApiCall { DuoAuthClient.pingService.ping() }
    }

    suspend fun check(): ApiResult<CheckResponse> {
        return safeApiCall { DuoAuthClient.checkService.check() }
    }

    suspend fun enroll(): ApiResult<EnrollResponse> {
        return safeApiCall { DuoAuthClient.enrollService.enroll() }
    }

    suspend fun enrollStatus(): ApiResult<EnrollStatusResponse> {
        return safeApiCall { DuoAuthClient.enrollStatusService.enrollStatus() }
    }

    suspend fun preAuth(): ApiResult<PreAuthResponse> {
        return safeApiCall { DuoAuthClient.preAuthService.preAuth() }
    }

    suspend fun auth(async: Boolean = false): ApiResult<AuthResponse> {
        return safeApiCall { DuoAuthClient.authService.auth(async) }
    }

    suspend fun authStatus(): ApiResult<AuthStatusResponse> {
        return safeApiCall { DuoAuthClient.authStatusService.authStatus() }
    }
}