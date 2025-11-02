package com.cineplusapp.cineplusspaapp.data.model

data class Movie(
    val id: Int,
    val title: String,
    val synopsis: String,
    val posterUrl: String?,
    val durationMin: Int,
    val rating: String,
    val genres: List<String>,
    val showtimes: List<String>
)
