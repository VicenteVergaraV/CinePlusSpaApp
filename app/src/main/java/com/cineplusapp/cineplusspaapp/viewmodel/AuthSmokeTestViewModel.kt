package com.cineplusapp.cineplusspaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
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
                status.value = "GET /auth/profile…"
                val me = api.getMyAuthProfile()
                Log.d(tag, "GET /auth/profile OK → email=${me.email}")
                status.value = "ME OK: ${me.email}"

                status.value = "Prueba terminada OK"

            } catch (e: Exception) {
                Log.e(tag, "Fallo en prueba", e)
                status.value = "Error: ${e.message}"
            }
        }
    }
}
