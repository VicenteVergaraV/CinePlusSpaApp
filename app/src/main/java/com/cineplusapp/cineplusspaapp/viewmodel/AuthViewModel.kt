package com.cineplusapp.cineplusspaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val api: ApiService,
    private val session: SessionManager
) : ViewModel() {

    private val tag = "AuthViewModel"

    // Texto simple para mostrar en la UI (estado del login)
    private val _status = MutableStateFlow("Sin iniciar sesión")
    val status: StateFlow<String> get() = _status

    /**
     * Llamar desde la Activity/Fragment:
     * viewModel.login(email, password)
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _status.value = "Iniciando sesión…"
                Log.d(tag, "POST /auth/login")

                // authRepo.login debe:
                // 1) llamar a api.login(...)
                // 2) guardar el access_token en SessionManager
                val result = authRepo.login(email, password)

                if (result.isFailure) {
                    val ex = result.exceptionOrNull()
                    Log.e(tag, "Login FAILED", ex)
                    _status.value = "Error en login: ${ex?.message ?: "desconocido"}"
                    return@launch
                }

                // Confirmar que el token se guardó bien
                val token = session.getAuthToken()
                Log.d(tag, "Login OK. accessToken=${token?.take(16)}…")
                _status.value = "Login correcto, token guardado"

                // Probar endpoint protegido usando el AuthInterceptor
                _status.value = "Consultando /auth/profile…"
                val me = api.getMyAuthProfile()
                Log.d(tag, "GET /auth/profile OK → email=${me.email}")
                _status.value = "Sesión activa: ${me.email}"

            } catch (e: Exception) {
                Log.e(tag, "Error en flujo de login", e)
                _status.value = "Error: ${e.message}"
            }
        }
    }

    /**
     * Si quieres una prueba rápida sin UI, puedes llamar a esto
     * desde la Activity para validar toda la cadena:
     *  - login
     *  - token
     *  - /auth/profile
     */
    fun smokeTestDemo() {
        // Usa credenciales hardcodeadas solo para pruebas
        login("admin@loquesea.com", "password123")
    }
}
