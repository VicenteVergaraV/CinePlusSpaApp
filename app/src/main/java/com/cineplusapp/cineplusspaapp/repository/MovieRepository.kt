package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun list(): Flow<List<MovieUi>>
    fun byId(id: Int): Flow<MovieUi?>
    suspend fun seedIfEmpty()
}
