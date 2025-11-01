package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Recuperacion del token de SessionManager
        val token = runBlocking {
            sessionManager.getAuthToken()
        }

        // En el caso de no haber token, continuar con la peticion original
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // Creacion de una nueva peticion con el token
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}