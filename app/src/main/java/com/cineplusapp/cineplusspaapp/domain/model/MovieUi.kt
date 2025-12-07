package com.cineplusapp.cineplusspaapp.domain.model

data class MovieUi(
    val id: String,
    val numericId: Int,
    val titulo: String,
    val director: String?,
    val genero: String?,
    val duracion: Int?,          // en minutos
    val sinopsis: String?,
    val posterUrl: String?,      // para mostrar en la lista/detalle
    val thumbnailUrl: String?,   // para miniaturas, si lo usas
    val trailerUrl: String?      // link a YouTube u otro
)

