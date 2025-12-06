package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.mappers.MovieMapper
import com.cineplusapp.cineplusspaapp.data.remote.PeliculaApiService
import com.cineplusapp.cineplusspaapp.domain.model.Movie
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

    override fun list(): Flow<List<MovieUi>> = flow {
        val response = apiService.getAllPeliculas()
        if (response.isSuccessful) {
            val remoteMovies = response.body()?.data ?: emptyList()
            emit(movieMapper.mapFromRemoteList(remoteMovies))
        } else {
            throw Exception("Error ${response.code()}: No se pudo obtener la lista de películas.")
        }
    }

    override fun byId(id: Int): Flow<MovieUi?> = flow {
        val response = apiService.getPeliculaById(id.toString())
        if (response.isSuccessful && response.body() != null) {
            val remoteMovie = response.body()!!.data
            if (remoteMovie != null) {
                emit(movieMapper.mapFromRemote(remoteMovie))
            } else {
                emit(null) // data vino null, mejor emitir null
            }
        } else if (response.code() == 404) {
            emit(null) // Película no encontrada
        } else {
            throw Exception("Error ${response.code()}: No se pudo obtener la película.")
        }
    }


    override suspend fun seedIfEmpty() {
        // No-op for remote repository
    }
}
