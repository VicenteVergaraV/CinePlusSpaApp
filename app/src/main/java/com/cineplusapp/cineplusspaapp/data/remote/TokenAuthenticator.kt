package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit // Puedes borrar este import si ya no se usa
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val session: SessionManager,
    // La siguiente dependencia ya no se usa, pero la dejamos para no romper Hilt todavía.
    @Named("authRetrofit") private val authRetrofit: Retrofit
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // El nuevo backend no soporta refresh token.
        // Limpiamos los tokens locales (si existen) para forzar un nuevo login la próxima vez.
        runBlocking {
            session.clearTokens()
        }

        // Devolvemos null para indicarle a OkHttp que la autenticación automática falló
        // y que no debe reintentar la petición. La llamada original fallará con un 401.
        return null
    }

    // La función responseCount ya no es necesaria, puedes eliminarla también.
    // private fun responseCount(response: Response): Int { ... }
}
