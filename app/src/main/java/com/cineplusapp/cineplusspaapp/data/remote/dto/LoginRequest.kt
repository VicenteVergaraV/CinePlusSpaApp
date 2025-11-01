package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName
// DTO para el login, datos que recibe el servidor
data class LoginRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("expiresInMins")
    val expiresInMins: Int = 30 // Para que el token expire dentro de 30 minutos
)