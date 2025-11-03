// data/local/entity/MovieEntity.kt
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
    val genresCsv: String,      // CSV: "accion|aventura|scifi"
    val showtimesCsv: String    // CSV: "2025-11-04 16:00|2025-11-04 19:00"
)
