package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUi(
    val loading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUi())
    val uiState: StateFlow<LoginUi> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<AuthTokens>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    /**
     * Validación local antes de llamar al backend
     */
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    private fun validate(email: String, password: String): Boolean {
        var valid = true
        var emailError: String? = null
        var passwordError: String? = null

        val e = email.trim()
        val p = password.trim()

        if (e.isBlank()) {
            emailError = "El correo es obligatorio"
            valid = false
        } else if (!EMAIL_REGEX.matches(e)) {
            emailError = "Formato de correo no válido"
            valid = false
        }

        if (p.isBlank()) {
            passwordError = "La contraseña es obligatoria"
            valid = false
        } else if (p.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            valid = false
        }

        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                generalError = null // limpio error general si lo había
            )
        }

        return valid
    }

    fun login(email: String, password: String) {
        // 1) Validación local
        if (!validate(email, password)) return

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, generalError = null) }

            try {
                val result = authRepository.login(email, password)

                result.fold(
                    onSuccess = { authTokens ->
                        // Guarda token
                        sessionManager.saveAuthToken(authTokens.access)

                        // Limpia errores
                        _uiState.update {
                            it.copy(
                                loading = false,
                                generalError = null
                            )
                        }

                        // Evento de éxito (para navegar)
                        _loginSuccess.emit(authTokens)
                    },
                    onFailure = { exception ->
                        val userMessage = mapExceptionToMessage(exception)

                        _uiState.update {
                            it.copy(
                                loading = false,
                                generalError = userMessage
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                val userMessage = mapExceptionToMessage(e)
                _uiState.update {
                    it.copy(
                        loading = false,
                        generalError = userMessage
                    )
                }
            }
        }
    }

    /**
     * Aquí se traduce excepciones técnicas a mensajes amigables para el usuario.
     */
    private fun mapExceptionToMessage(e: Throwable): String {
        return when (e) {
            is java.io.IOException -> {
                // sin conexión, timeout, etc.
                "No se pudo conectar. Verifica tu conexión a internet."
            }
            is retrofit2.HttpException -> {
                when (e.code()) {
                    400, 401 -> "Correo o contraseña incorrectos."
                    403 -> "No tienes permisos para acceder."
                    500 -> "El servidor está teniendo problemas. Intenta más tarde."
                    else -> "Ocurrió un error (${e.code()}). Intenta nuevamente."
                }
            }
            else -> {
                // Evita mostrar el .message crudo al usuario
                "Ha ocurrido un error inesperado. Intenta nuevamente."
            }
        }
    }
}
