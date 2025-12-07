package com.cineplusapp.cineplusspaapp.domain.repository

import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.data.remote.dto.UpdateUsuarioProfileRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import com.cineplusapp.cineplusspaapp.data.remote.dto.UsuarioProfileDto

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthTokens>

    suspend fun register(
        email: String,
        password: String,
        nombre: String
    ): Result<AuthTokens>

    suspend fun getMe(): Result<UserDto>

    suspend fun getMyProfile(): Result<UsuarioProfileDto>

    suspend fun updateMyProfile(updateRequest: UpdateUsuarioProfileRequest): Result<UsuarioProfileDto>

    suspend fun logout()
}