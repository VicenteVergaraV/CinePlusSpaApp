package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val session: SessionManager,
    @Named("authRetrofit") private val authRetrofit: Retrofit // Retrofit "limpio" sin interceptor/authenticator
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Evitar loop infinito (máx. 2 intentos)
        if (responseCount(response) >= 2) return null

        // No refrescar mientras se llama a login/refresh
        val path = response.request.url.encodedPath
        if (path.endsWith("/user/login") || path.endsWith("/auth/refresh")) return null

        // Tomar refresh token actual
        val refresh = runBlocking { session.getRefreshToken() } ?: return null

        // Crear API de refresh usando el retrofit "limpio"
        val api = authRetrofit.create(AuthApi::class.java)

        // *** OJO NOMBRE CONSISTENTE: newTokens (camelCase) ***
        val newTokens = try {
            runBlocking { api.refresh(RefreshRequest(refresh)) }
        } catch (e: HttpException) {
            runBlocking { session.clearTokens() }
            return null
        } catch (e: IOException) {
            // sin red: no intentes de nuevo
            return null
        } catch (e: Exception) {
            runBlocking { session.clearTokens() }
            return null
        }

        // Validar respuesta
        val access = newTokens.token?.trim().orEmpty()
        if (access.isBlank()) {
            runBlocking { session.clearTokens() }
            return null
        }

        // Guardar y reintentar con el header nuevo
        runBlocking { session.saveTokens(access, newTokens.refreshToken) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $access")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) { count++; r = r.priorResponse }
        return count
    }
}
