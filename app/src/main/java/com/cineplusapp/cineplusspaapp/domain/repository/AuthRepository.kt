// AuthRepository.kt
package com.cineplusapp.cineplusspaapp.domain.repository

import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthTokens>

    suspend fun register(
        email: String,
        password: String,
        nombre: String
    ): Result<AuthTokens>

    suspend fun logout()
}