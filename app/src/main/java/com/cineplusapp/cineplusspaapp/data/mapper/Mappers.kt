package com.cineplusapp.cineplusspaapp.data.mapper

import com.cineplusapp.cineplusspaapp.data.local.entity.MovieEntity
import com.cineplusapp.cineplusspaapp.data.local.entity.ProductEntity
import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.data.model.Product
import com.cineplusapp.cineplusspaapp.data.model.ProductType

private fun String.csvToList(): List<String> =
    if (isBlank()) emptyList() else split("|").map { it.trim() }

private fun List<String>.toCsv(): String = joinToString("|")

fun MovieEntity.toDomain() = Movie(
    id, title, synopsis, posterUrl, durationMin, rating,
    genresCsv.csvToList(), showtimesCsv.csvToList()
)

fun Movie.toEntity() = MovieEntity(
    id, title, synopsis, posterUrl, durationMin, rating,
    genres.toCsv(), showtimes.toCsv()
)

fun ProductEntity.toDomain() = Product(
    id, name, description, imageUrl, price,
    when (type.lowercase()) {
        "popcorn" -> ProductType.POPCORN
        "drink"   -> ProductType.DRINK
        else      -> ProductType.COMBO
    }
)

fun Product.toEntity() = ProductEntity(
    id, name, description, imageUrl, price, type.name.lowercase()
)
