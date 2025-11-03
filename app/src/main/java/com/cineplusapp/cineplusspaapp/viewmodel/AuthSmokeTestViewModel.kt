package com.cineplusapp.cineplusspaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthSmokeTestViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val api: ApiService,
    private val session: SessionManager
) : ViewModel() {

    val status = MutableStateFlow("Iniciando prueba…")
    private val tag = "AuthSmoke"

    init {
        viewModelScope.launch {
            try {
                status.value = "Login…"
                Log.d(tag, "POST /user/login")
                val r = authRepo.login("demo", "demo123") // <-- usa credenciales válidas de tu mock
                if (r.isFailure) throw r.exceptionOrNull() ?: RuntimeException("login failed")

                val at = session.getAuthToken()
                Log.d(tag, "Login OK. accessToken=${at?.take(16)}…")
                status.value = "Login OK"

                // Probar endpoint protegido
                status.value = "GET /user/me…"
                val me = api.getCurrentUser()
                Log.d(tag, "GET /user/me OK → username=${me.username}")
                status.value = "ME OK: ${me.username}"

                // Probar refresh SOLO si tienes refreshToken
                val rt = session.getRefreshToken()
                if (!rt.isNullOrBlank()) {
                    status.value = "Forzando 401 (rompiendo access)…"
                    session.saveAuthToken("invalid-token-for-test")
                    val me2 = api.getCurrentUser() // debe disparar refresh y reintentar
                    val newAt = session.getAuthToken()
                    Log.d(tag, "REFRESH OK. newAccess=${newAt?.take(16)}… username=${me2.username}")
                    status.value = "Refresh OK"
                } else {
                    Log.d(tag, "Sin refreshToken; se omite prueba de refresh")
                }

                status.value = "Prueba terminada OK"

            } catch (e: Exception) {
                Log.e(tag, "Fallo en prueba", e)
                status.value = "Error: ${e.message}"
            }
        }
    }
}
