package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest

interface AuthRepository {
    suspend fun login(user: String, pass: String): Result<Unit>
    suspend fun logout()
}
