package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val session: SessionManager
) : AuthRepository {

    override suspend fun login(user: String, pass: String): Result<Unit> = runCatching {
        val resp = api.login(LoginRequest(user, pass))
        if (resp.refreshToken.isNullOrBlank()) {
            session.saveAuthToken(resp.accessToken)
        } else {
            session.saveTokens(access = resp.accessToken, refresh = resp.refreshToken)
        }
    }


    override suspend fun logout() {
        session.clear()
    }
}
