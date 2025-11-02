package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.dao.MovieDao
import com.cineplusapp.cineplusspaapp.data.mapper.toDomain
import com.cineplusapp.cineplusspaapp.data.mapper.toEntity
import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryLocal @Inject constructor(
    private val dao: MovieDao
) : MovieRepository {

    override val movies: Flow<List<Movie>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun byId(id: Int): Flow<Movie?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun seedIfEmpty() {
        // Simple seed local
        // Si ya hay datos, no hace nada
        // Carga mínima:
        val current = dao.observeAll()
        dao.clear()
        val seed = listOf(
            Movie(
                id = 1,
                title = "Galaxias 9",
                synopsis = "Aventura espacial para toda la familia.",
                posterUrl = "https://picsum.photos/400/600?random=11",
                durationMin = 128,
                rating = "ATP",
                genres = listOf("Aventura", "Ciencia Ficción"),
                showtimes = listOf("2025-11-02 16:30", "2025-11-02 19:30", "2025-11-02 22:15")
            ),
            Movie(
                id = 2,
                title = "Ciudad Oscura",
                synopsis = "Thriller policial en una ciudad costera.",
                posterUrl = "https://picsum.photos/400/600?random=12",
                durationMin = 103,
                rating = "14+",
                genres = listOf("Thriller"),
                showtimes = listOf("2025-11-02 18:00", "2025-11-02 21:10")
            )
        )
        dao.upsertAll(seed.map { it.toEntity() })
    }
}
