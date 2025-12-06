package com.cineplusapp.cineplusspaapp.repository.impl

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.ApiResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterRequest
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val session: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthTokens> =
        runCatching {
            // Ahora recibimos el wrapper { success, message, data }
            val apiResponse: ApiResponse<LoginResponse> =
                api.login(LoginRequest(email, password))

            if (!apiResponse.success) {
                // El backend te está diciendo que no fue exitoso
                throw RuntimeException(apiResponse.message)
            }

            val body = apiResponse.data
                ?: throw RuntimeException("Respuesta sin datos de login")

            val token = body.accessToken
            if (token.isBlank()) {
                throw RuntimeException("Access token vacío")
            }

            // Guardar el token en DataStore
            session.saveAuthToken(token)

            // Devolver AuthTokens por si se usa más adelante
            AuthTokens(
                access = token,
                refresh = null
            )
        }

    override suspend fun register(
        email: String,
        password: String,
        nombre: String
    ): Result<AuthTokens> =
        runCatching {
            val apiResponse = api.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    role = "USUARIO",
                    nombre = nombre
                )
            )

            if (!apiResponse.success) {
                throw RuntimeException(apiResponse.message)
            }

            val body = apiResponse.data
                ?: throw RuntimeException("Respuesta sin datos de registro")

            val token = body.accessToken
            if (token.isNullOrBlank()) {
                throw RuntimeException("Access token vacío en registro")
            }

            // Guardar el token en DataStore
            session.saveAuthToken(token)

            // Devolver AuthTokens
            AuthTokens(
                access = token,
                refresh = null
            )
        }



    override suspend fun logout() {
        session.clearTokens()
    }
}
