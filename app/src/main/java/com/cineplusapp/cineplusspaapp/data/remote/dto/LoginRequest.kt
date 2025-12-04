package com.cineplusapp.cineplusspaapp.data.remote.dto

// DTO para el login, datos que recibe el servidor de Render
data class LoginRequest(
    val email: String,
    val password: String
)