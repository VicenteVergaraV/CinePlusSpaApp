package com.cineplusapp.cineplusspaapp.domain.use_case

import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository

class AuthUseCase(private val authRepository: AuthRepository) {
    suspend fun login(user: String, pass: String): Result<AuthTokens> {
        return authRepository.login(user, pass)
    }

    suspend fun logout() {
        authRepository.logout()
    }
}
