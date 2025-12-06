package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Si ya trae Authorization, no tocar
        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        val path = original.url.encodedPath

        // Evitar agregar token en login/registro/refresh
        val isAuthCall =
            path.endsWith("/auth/login") ||
                    path.endsWith("/auth/register") ||
                    path.endsWith("/auth/refresh")

        if (isAuthCall) {
            return chain.proceed(original)
        }

        // Obtener token desde SessionManager
        val token = runBlocking { sessionManager.getAuthToken() }

        if (token.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val authed = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authed)
    }

}
