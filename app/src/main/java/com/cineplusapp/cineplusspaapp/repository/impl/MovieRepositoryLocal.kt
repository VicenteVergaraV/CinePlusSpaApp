package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.mappers.MovieMapper
import com.cineplusapp.cineplusspaapp.data.remote.PeliculaApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.CrearMovieRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.UpdateMovieRequest
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryRemote @Inject constructor(
    // 1. Inyectamos la interfaz de Retrofit
    private val apiService: PeliculaApiService,
    // 2. Inyectamos el Mapper para transformar los datos de red
    private val movieMapper: MovieMapper
) : MovieRepository {


    /**
     * Obtiene todas las películas (Lista).
     * Usa PeliculaListResponse y el Mapper.
     */
    override suspend fun getMovies(): List<MovieUi> {
        val response = apiService.getAllPeliculas()

        if (response.isSuccessful) {
            // CORRECCIÓN CLAVE: Acceder a la propiedad .data del DTO de respuesta
            val remoteMovies = response.body()?.data ?: emptyList()
            return movieMapper.mapFromRemoteList(remoteMovies)
        } else {
            // Manejo de errores de HTTP (4xx, 5xx)
            throw Exception("Error ${response.code()}: No se pudo obtener la lista de películas.")
        }
    }

    /**
     * Obtiene una película por su ID (Detalle).
     * Usa PeliculaSingleResponse y el Mapper.
     */
    override suspend fun getMovieById(id: String): MovieUi? {
        val response = apiService.getPeliculaById(id)

        if (response.isSuccessful && response.body() != null) {
            // CORRECCIÓN CLAVE: Acceder a la propiedad .data del DTO de respuesta
            val remoteMovie = response.body()!!.data
            return movieMapper.mapFromRemote(remoteMovie)
        } else if (response.code() == 404) {
            return null // Película no encontrada
        } else {
            throw Exception("Error ${response.code()}: No se pudo obtener la película.")
        }
    }

    // --- FUNCIONES CRUD ADICIONALES (Opcional, pero necesarias para tu Backend) ---

    /**
     * Crea una nueva película. Requiere token (gestionado por AuthInterceptor).
     */
    suspend fun createMovie(request: CrearMovieRequest): MovieUi {
        val response = apiService.createPelicula(request)
        if (response.isSuccessful && response.body()?.data != null) {
            return movieMapper.mapFromRemote(response.body()!!.data)
        }
        throw Exception("Error al crear la película: Código ${response.code()}")
    }

    /**
     * Actualiza una película. Requiere token.
     */
    suspend fun updateMovie(id: String, request: UpdateMovieRequest): MovieUi {
        val response = apiService.updatePelicula(id, request)
        if (response.isSuccessful && response.body()?.data != null) {
            return movieMapper.mapFromRemote(response.body()!!.data)
        }
        throw Exception("Error al actualizar la película: Código ${response.code()}")
    }

    /**
     * Elimina una película. Requiere token.
     */
    suspend fun deleteMovie(id: String): Boolean {
        val response = apiService.deletePelicula(id)
        if (response.isSuccessful) {
            // Asume que si es 200/OK, la eliminación fue exitosa (el backend devuelve { success: true, ... })
            return response.body()?.success ?: false
        }
        throw Exception("Error al eliminar la película: Código ${response.code()}")
    }
}