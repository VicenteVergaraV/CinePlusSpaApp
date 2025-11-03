package com.cineplusapp.cineplusspaapp.data.model

data class Movie(
    val id: Int,
    val title: String,
    val synopsis: String = "",
    val posterUrl: String? = null,
    val durationMin: Int = 0,
    val rating: String = "",
    val genres: List<String> = emptyList(),
    val showtimes: List<String> = emptyList()
)