package com.cineplusapp.cineplusspaapp.data.remote.dto

data class PeliculaRequest(
    val titulo: String,
    val director: String? = null,
    val genero: String? = null,
    val duracion: Int? = null,
    val sinopsis: String? = null,
    val poster: String? = null,
    val trailer: String? = null,
    val imagen: String? = null,
    val imagenThumbnail: String? = null
)
