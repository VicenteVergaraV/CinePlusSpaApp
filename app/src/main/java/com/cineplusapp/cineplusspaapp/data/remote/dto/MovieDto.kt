package com.cineplusapp.cineplusspaapp.data.remote.dto

import  com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("_id")
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null
)
