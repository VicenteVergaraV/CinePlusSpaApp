package com.cineplusapp.cineplusspaapp.data.remote.dto

data class PeliculaDto(
    val _id: String?,
    val titulo: String,
    val director: String?,
    val genero: String?,
    val duracion: Int?,
    val sinopsis: String?,
    val poster: String?,
    val trailer: String?,
    val imagen: String?,
    val imagenThumbnail: String?
)
