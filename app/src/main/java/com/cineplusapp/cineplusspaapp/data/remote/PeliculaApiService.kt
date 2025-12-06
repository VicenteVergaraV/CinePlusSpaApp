package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.remote.dto.ApiResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.PeliculaDto
import com.cineplusapp.cineplusspaapp.data.remote.dto.PeliculaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PeliculaApiService {

    // --- LISTAR TODAS LAS PELÍCULAS ---
    @GET("pelicula")
    suspend fun getAllPeliculas(): Response<ApiResponse<List<PeliculaDto>>>

    // --- OBTENER UNA PELÍCULA POR ID ---
    @GET("pelicula/{id}")
    suspend fun getPeliculaById(
        @Path("id") id: String
    ): Response<ApiResponse<PeliculaDto>>

    // --- CREAR NUEVA PELÍCULA ---
    @POST("pelicula")
    suspend fun createPelicula(
        @Body pelicula: PeliculaRequest
    ): Response<ApiResponse<PeliculaDto>>

    // --- ACTUALIZAR PELÍCULA (cuando tengas el endpoint backend listo) ---
    @PATCH("pelicula/{id}")
    suspend fun updatePelicula(
        @Path("id") id: String,
        @Body pelicula: PeliculaRequest   // por ahora reutilizamos el mismo DTO
    ): Response<ApiResponse<PeliculaDto>>

    // --- ELIMINAR PELÍCULA ---
    @DELETE("pelicula/{id}")
    suspend fun deletePelicula(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>
}
