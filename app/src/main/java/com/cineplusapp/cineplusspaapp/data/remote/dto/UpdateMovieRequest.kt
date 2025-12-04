package com.cineplusapp.cineplusspaapp.data.remote.dto

data class UpdateMovieRequest(
    val title: String? = null,
    val plot: String? = null,
    val year: Int? = null,
    val genres: List<String>? = null,
    val cast: List<String>? = null,
    val rated: String? = null,
    val poster: String? = null
)
