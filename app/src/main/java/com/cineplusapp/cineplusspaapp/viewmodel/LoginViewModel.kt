// LoginViewModel.kt
package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens

data class LoginUi(val loading: Boolean = false, val error: String? = null)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val session: SessionManager
) : ViewModel() {

    var ui by mutableStateOf(LoginUi())
        private set

    fun login(
        user: String,
        pass: String,
        onSuccess: (access: String, refresh: String?) -> Unit
    ) = viewModelScope.launch {
        ui = ui.copy(loading = true, error = null)

        // Regla local: permitir demo/demo123 o email registrado
        val isDemo = (user == "demo" && pass == "demo123")
        val registeredOk = session.isUserRegistered(user) && session.validateLocalCredentials(user, pass)

        if (!isDemo && !registeredOk) {
            ui = ui.copy(loading = false, error = "Usuario no registrado. Crea tu cuenta.")
            return@launch
        }


        if (isDemo) {
            onSuccess("DEMO_ACCESS_TOKEN", "DEMO_REFRESH_TOKEN")
            ui = ui.copy(loading = false)
            return@launch
        }

        // Usuarios registrados: procede contra backend real
        val r = authRepo.login(user, pass)
        ui = if (r.isSuccess) {
            val t = r.getOrNull()!!
            onSuccess(t.access, t.refresh)
            ui.copy(loading = false)
        } else {
            ui.copy(loading = false, error = r.exceptionOrNull()?.message ?: "Error de autenticación")
        }
    }
}