package com.cineplusapp.cineplusspaapp.data.remote


import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.UpdateUsuarioProfileRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import com.cineplusapp.cineplusspaapp.data.remote.dto.UsuarioProfileDto
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
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("auth/profile")
    suspend fun getMyAuthProfile(): UserDto


    // ---------- USUARIO PROFILE ----------

    // Perfil del usuario autenticado (dominio usuario-profile)
    @GET("usuario-profile/me")
    suspend fun getMyUsuarioProfile(): UsuarioProfileDto

    @PUT("usuario-profile/me")
    suspend fun updateMyUsuarioProfile(
        @Body body: UpdateUsuarioProfileRequest
    ): UsuarioProfileDto

    // Endpoints “admin” (si el backend los expone y tu usuario tiene permisos)
    @GET("usuario-profile")
    suspend fun getAllProfiles(): List<UsuarioProfileDto>

    @GET("usuario-profile/{userId}")
    suspend fun getProfileByUserId(
        @Path("userId") userId: String
    ): UsuarioProfileDto
}