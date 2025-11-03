package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movie")
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val movieId: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)
