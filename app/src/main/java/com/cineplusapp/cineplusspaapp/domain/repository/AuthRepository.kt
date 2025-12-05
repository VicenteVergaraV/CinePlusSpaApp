package com.cineplusapp.cineplusspaapp.domain.repository

import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens

interface AuthRepository {
    suspend fun login(user: String, pass: String): Result<AuthTokens>
    suspend fun logout()
}
