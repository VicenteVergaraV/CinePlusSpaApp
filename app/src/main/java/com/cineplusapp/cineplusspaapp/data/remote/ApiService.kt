package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ---------- AUTH ----------

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    @GET("auth/profile")
    suspend fun getMyAuthProfile(): UserDto


    // ---------- USUARIO PROFILE ----------

    @GET("usuario-profile/me")
    suspend fun getMyUsuarioProfile(): UsuarioProfileDto

    @PUT("usuario-profile/me")
    suspend fun updateMyUsuarioProfile(
        @Body body: UpdateUsuarioProfileRequest
    ): UsuarioProfileDto

    @GET("usuario-profile")
    suspend fun getAllProfiles(): List<UsuarioProfileDto>

    @GET("usuario-profile/{userId}")
    suspend fun getProfileByUserId(
        @Path("userId") userId: String
    ): UsuarioProfileDto


    // ---------- PELICULA ----------

    // Crear película (el token lo agrega el AuthInterceptor)
    @POST("pelicula")
    suspend fun crearPelicula(
        @Body body: PeliculaRequest
    ): ApiResponse<PeliculaDto>

    // (Cuando tengas GET /api/pelicula en el backend)
    @GET("pelicula")
    suspend fun getPeliculas(): ApiResponse<List<PeliculaDto>>
}
