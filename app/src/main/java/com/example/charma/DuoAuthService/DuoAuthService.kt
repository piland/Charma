package com.example.charma.DuoAuthService

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DuoAuthService {
    @GET("/")
    suspend fun ping(): Response<PingResponse>

    @GET("/")
    suspend fun check(): Response<CheckResponse>

    @GET("/")
    suspend fun logo(): Response<LogoResponse>

    // Repeat for other endpoints

    companion object {
        // Each method returns a unique Retrofit instance for its respective base URL
        val pingService: DuoAuthService by lazy {
            Retrofit.Builder()
                .baseUrl("https://ping-nbdrl3ye6a-uc.a.run.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DuoAuthService::class.java)
        }

        val checkService: DuoAuthService by lazy {
            Retrofit.Builder()
                .baseUrl("https://check-nbdrl3ye6a-uc.a.run.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DuoAuthService::class.java)
        }

        val logoService: DuoAuthService by lazy {
            Retrofit.Builder()
                .baseUrl("https://logo-nbdrl3ye6a-uc.a.run.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DuoAuthService::class.java)
        }

        // Define similar lazy properties for each endpoint
    }
}
