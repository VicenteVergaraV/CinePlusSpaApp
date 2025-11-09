package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {

    override suspend fun login(user: String, pass: String): Result<AuthTokens> = try {
        val body = api.login(LoginRequest(username = user, password = pass)) // /user/login
        val access = body.accessToken?.trim().orEmpty()
        if (access.isBlank()) Result.failure(IllegalStateException("Access token vacío"))
        else Result.success(AuthTokens(access = access, refresh = body.refreshToken))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() { /* endpoint / limpiar tokens */ }
}