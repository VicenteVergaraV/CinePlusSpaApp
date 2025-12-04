package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UsuarioProfileDto(
    // Id del documento de perfil en Mongo
    @SerializedName("_id")
    val id: String,

    // Referencia al usuario dueño del perfil (ajusta el nombre si tu API usa "userId")
    @SerializedName("user")
    val userId: String? = null,

    val nombre: String? = null,
    val telefono: String? = null,

    // Por si el backend agrega estos campos (típicos en Mongoose)
    val createdAt: String? = null,
    val updatedAt: String? = null
)
