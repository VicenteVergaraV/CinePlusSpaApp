package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.mappers.MovieMapper
import com.cineplusapp.cineplusspaapp.data.remote.PeliculaApiService
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryLocal @Inject constructor(
    private val apiService: PeliculaApiService,
    private val movieMapper: MovieMapper
) : MovieRepository {

    private var movies: List<MovieUi> = emptyList()

    override fun list(): Flow<List<MovieUi>> = flow {
        emit(movies)
    }

    override fun byId(id: Int): Flow<MovieUi?> = flow {
        emit(movies.find { it.numericId == id })
    }

    override suspend fun seedIfEmpty() {
        if (movies.isEmpty()) {
            val response = apiService.getAllPeliculas()
            if (response.isSuccessful) {
                response.body()?.data?.let { peliculas ->
                    movies = movieMapper.mapFromRemoteList(peliculas)
                }
            }
        }
    }
}
