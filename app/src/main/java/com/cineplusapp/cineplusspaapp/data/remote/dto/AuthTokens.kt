package com.cineplusapp.cineplusspaapp.data.remote.dto

data class AuthTokens(
    val access: String,
    val refresh: String? = null
)
