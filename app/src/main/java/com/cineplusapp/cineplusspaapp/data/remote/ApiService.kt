package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import com.cineplusapp.cineplusspaapp.data.remote.dto.UsersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Autenticar Usuario
    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Obtener usuario Actual
    @GET("user/me")
    suspend fun getCurrentUser(): UserDto

    // Obtener una Lista de Usuarios
    @GET("users")
    suspend fun getUsers(): UsersResponse

    // Buscar Usuarios por Nombre
    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): UsersResponse

    // Obtener Usuario por ID
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto
}