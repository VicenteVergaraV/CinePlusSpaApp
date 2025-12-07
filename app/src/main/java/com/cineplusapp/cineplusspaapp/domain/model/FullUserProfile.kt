package com.cineplusapp.cineplusspaapp.domain.model

data class FullUserProfile(
    val id: String,
    val email: String,
    val role: String,
    val nombre: String?,
    val telefono: String?,
    val avatar: String?
)