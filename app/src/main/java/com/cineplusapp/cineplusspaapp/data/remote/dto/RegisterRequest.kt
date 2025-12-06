package com.cineplusapp.cineplusspaapp.data.remote.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String = "USUARIO",
    val nombre: String
)
