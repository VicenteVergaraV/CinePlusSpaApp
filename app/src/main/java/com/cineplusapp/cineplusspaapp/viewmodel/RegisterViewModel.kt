package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUi(
    val loading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val session: SessionManager
) : ViewModel() {

    var ui by mutableStateOf(RegisterUi())
        private set

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            ui = RegisterUi(loading = true)

            val n = name.trim()
            val e = email.trim().lowercase()
            val p = pass.trim()

            // Validaciones simples
            if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
                ui = RegisterUi(error = "Completa todos los campos.")
                return@launch
            }
            if (!e.contains('@') || !e.contains('.')) {
                ui = RegisterUi(error = "Email no válido.")
                return@launch
            }
            if (p.length < 6) {
                ui = RegisterUi(error = "La contraseña debe tener al menos 6 caracteres.")
                return@launch
            }

            // ¿Ya existe?
            if (session.isUserRegistered(e)) {
                ui = RegisterUi(error = "El usuario ya está registrado.")
                return@launch
            }

            // Registrar en DataStore local (hash interno en SessionManager)
            try {
                session.registerUser(email = e, name = n, pass = p)
                ui = RegisterUi(done = true)
                onSuccess()
            } catch (ex: Exception) {
                ui = RegisterUi(error = ex.message ?: "Error al registrar.")
            }
        }
    }
}
