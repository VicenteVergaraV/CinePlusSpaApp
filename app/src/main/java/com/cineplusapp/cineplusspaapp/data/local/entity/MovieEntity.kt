package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val synopsis: String,
    val posterUrl: String?,
    val durationMin: Int,
    val rating: String,
    val genresCsv: String,      // guardamos géneros como CSV
    val showtimesCsv: String    // guardamos funciones como CSV
)
