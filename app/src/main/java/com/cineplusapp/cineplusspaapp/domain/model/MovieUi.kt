package com.cineplusapp.cineplusspaapp.domain.model

data class MovieUi(
    val id: String,
    val title: String,
    val year: Int,
    val plot: String,
    val posterUrl: String,
    val genres: List<String>,
    val rating: Double
)
