package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.model.Movie
import kotlinx.coroutines.flow.Flow
interface MovieRepository {
    fun list(): Flow<List<Movie>>
    fun byId(id: Int): Flow<Movie?>

    suspend fun seedIfEmpty()
}

