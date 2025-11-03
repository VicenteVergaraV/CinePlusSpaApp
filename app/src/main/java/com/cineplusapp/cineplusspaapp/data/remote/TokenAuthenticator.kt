package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val session: SessionManager,
    @Named("authRetrofit") private val authRetrofit: Retrofit  // <- el de AUTH
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        // no refrescar en login/refresh
        val path = response.request.url.encodedPath
        if (path.endsWith("/user/login") || path.endsWith("/auth/refresh")) return null

        val refresh = runBlocking { session.getRefreshToken() } ?: return null
        val api = authRetrofit.create(AuthApi::class.java)

        val newTokens = try {
            runBlocking { api.refresh(RefreshRequest(refresh)) }
        } catch (_: Exception) {
            runBlocking { session.clear() }
            return null
        }

        runBlocking { session.saveTokens(newTokens.token, newTokens.refreshToken) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.token}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) { count++; r = r.priorResponse }
        return count
    }
}
