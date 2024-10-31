package com.example.charma.DuoAuthService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitPing = Retrofit.Builder()
    .baseUrl("https://ping-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitCheck = Retrofit.Builder()
    .baseUrl("https://check-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitLogo = Retrofit.Builder()
    .baseUrl("https://logo-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitEnroll = Retrofit.Builder()
    .baseUrl("https://enroll-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitPreAuth = Retrofit.Builder()
    .baseUrl("https://enrollstatus-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitAuth = Retrofit.Builder()
    .baseUrl("https://auth-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitAuthStatus = Retrofit.Builder()
    .baseUrl("https://authstatus-nbdrl3ye6a-uc.a.run.app")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
