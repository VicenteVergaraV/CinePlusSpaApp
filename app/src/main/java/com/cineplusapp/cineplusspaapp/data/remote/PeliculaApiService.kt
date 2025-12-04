package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.remote.dto.CrearMovieRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.ImageUploadResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.PeliculaListResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.PeliculaSingleResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.SuccessResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.UpdateMovieRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PeliculaApiService {

    // --- MÉTODOS CRUD BÁSICOS ---

    // @Get() -> Listar todos los Peliculas
    @GET("pelicula")
    suspend fun getAllPeliculas(): Response<PeliculaListResponse>

    // @Get(':id') -> Obtener Pelicula por ID
    @GET("pelicula/{id}")
    suspend fun getPeliculaById(@Path("id") id: String): Response<PeliculaSingleResponse>

    // @Post() -> Crear nuevo Pelicula (Necesita token)
    @POST("pelicula")
    suspend fun createPelicula(@Body pelicula: CrearMovieRequest): Response<PeliculaSingleResponse>

    // @Patch(':id') -> Actualizar Pelicula (Necesita token)
    @PATCH("pelicula/{id}")
    suspend fun updatePelicula(
        @Path("id") id: String,
        @Body pelicula: UpdateMovieRequest
    ): Response<PeliculaSingleResponse>

    // @Delete(':id') -> Eliminar Pelicula (Necesita token)
    @DELETE("pelicula/{id}")
    suspend fun deletePelicula(@Path("id") id: String): Response<SuccessResponse>

    // --- MANEJO DE IMAGEN (Multipart) ---

    // @Post(':id/upload-image') -> Subir imagen (Requiere Multipart)
    @Multipart
    @POST("pelicula/{id}/upload-image")
    suspend fun uploadPeliculaImage(
        @Path("id") id: String,
        // 'file' debe coincidir con el nombre de campo 'file' que espera el backend (Fastify/Multer)
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>
}