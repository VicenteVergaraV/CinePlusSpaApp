package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.cineplusapp.cineplusspaapp.data.model.Movie

data class PeliculaListResponse(
    val success: Boolean,
    val data: List<Movie>,
    val total: Int
)

data class PeliculaSingleResponse(
    val success: Boolean,
    val data: Movie,
    val message: String?
)

data class SuccessResponse(
    val success: Boolean,
    val message: String
)

data class ImageUploadResponse(
    val success: Boolean,
    val message: String,
    val data: ImageUploadData
)

data class ImageUploadData(
    val pelicula: Movie,
    val upload: UploadResult
)

data class UploadResult(
    val url: String,
    val thumbnailUrl: String
)