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

        // Evitar login/refresh
        val path = original.url.encodedPath
        if (path.endsWith("/user/login") || path.endsWith("/auth/refresh")) {
            return chain.proceed(original)
        }

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
