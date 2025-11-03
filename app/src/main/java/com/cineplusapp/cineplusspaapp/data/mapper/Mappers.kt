// data/mapper/Mappers.kt
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
    id = id,
    title = title,
    synopsis = synopsis,
    posterUrl = posterUrl,
    durationMin = durationMin,
    rating = rating,
    genres = genresCsv.csvToList(),
    showtimes = showtimesCsv.csvToList()
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    title = title,
    synopsis = synopsis,
    posterUrl = posterUrl,
    durationMin = durationMin,
    rating = rating,
    genresCsv = genres.toCsv(),
    showtimesCsv = showtimes.toCsv()
)

fun ProductEntity.toDomain() = Product(
    id, name, description, imageUrl, price,
    when (type.lowercase()) {
        "popcorn" -> ProductType.POPCORN
        "drink"           -> ProductType.DRINK
        "combo"           -> ProductType.COMBO
        else              -> ProductType.COMBO
    }
)

fun Product.toEntity() = ProductEntity(
    id, name, description, imageUrl, price, type.name.lowercase()
)
