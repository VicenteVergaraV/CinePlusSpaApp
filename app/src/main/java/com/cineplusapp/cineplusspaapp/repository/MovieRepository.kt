package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import kotlinx.coroutines.flow.Flow
interface MovieRepository {

    suspend fun getMovies(): List<MovieUi>

    suspend fun getMovieById(id: String): MovieUi?
}

