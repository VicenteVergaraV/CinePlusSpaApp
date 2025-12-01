package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(user: String, pass: String): Result<AuthTokens> {
        return try {
            val response = apiService.login(LoginRequest(user, pass))
            val authTokens = AuthTokens(response.accessToken, response.refreshToken)
            sessionManager.saveTokens(authTokens.access, authTokens.refresh)
            Result.success(authTokens)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Result.failure(Exception("Credenciales inválidas"))
            } else {
                Result.failure(Exception("Error de red"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Error de red"))
        } catch (e: Exception) {
            Result.failure(Exception("Un error desconocido ocurrió"))
        }
    }

    override suspend fun logout() {
        sessionManager.clearTokens()
    }
}