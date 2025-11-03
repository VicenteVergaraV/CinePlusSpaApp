// repository/impl/MovieRepositoryLocal.kt
package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.dao.MovieDao
import com.cineplusapp.cineplusspaapp.data.local.entity.MovieEntity
import com.cineplusapp.cineplusspaapp.data.mapper.toDomain
import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryLocal @Inject constructor(
    private val dao: MovieDao
) : MovieRepository {

    override fun list(): Flow<List<Movie>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun byId(id: Int): Flow<Movie?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            val seed = listOf(
                MovieEntity(
                    id = 1,
                    title = "CinePlus: Origen",
                    synopsis = "La historia de la app 😉",
                    posterUrl = null,
                    durationMin = 118,
                    rating = "PG-13",
                    genresCsv = "accion|aventura|scifi",
                    showtimesCsv = "2025-11-02 20:00|2025-11-03 18:00"
                ),
                MovieEntity(
                    id = 2,
                    title = "Comedia Plus",
                    synopsis = "Risas y más risas.",
                    posterUrl = null,
                    durationMin = 95,
                    rating = "ATP",
                    genresCsv = "comedia",
                    showtimesCsv = "2025-11-04 16:00|2025-11-04 19:00"
                )
            )
            dao.insertAll(seed)
        }
    }
}
