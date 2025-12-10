package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class RegisterUi(
    val loading: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val done: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    var ui by mutableStateOf(RegisterUi())
        private set

    /**
     * Limpia todos los errores para cuando el usuario escriba nuevamente
     */
    fun clearErrors() {
        ui = ui.copy(
            nameError = null,
            emailError = null,
            passwordError = null,
            generalError = null
        )
    }

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    private fun validate(name: String, email: String, pass: String): Boolean {
        var nameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var valid = true

        val n = name.trim()
        val e = email.trim()
        val p = pass.trim()

        if (n.isEmpty()) {
            nameError = "El nombre es obligatorio."
            valid = false
        }

        if (e.isEmpty()) {
            emailError = "El correo es obligatorio."
            valid = false
        } else if (!EMAIL_REGEX.matches(e)) {
            emailError = "Email no válido."
            valid = false
        }

        if (p.isEmpty()) {
            passwordError = "La contraseña es obligatoria."
            valid = false
        } else if (p.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres."
            valid = false
        }

        ui = ui.copy(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            generalError = null,  // se limpia el error general
            loading = false
        )

        return valid
    }

    fun register(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            // 1) Validación local
            if (!validate(name, email, pass)) return@launch

            ui = ui.copy(loading = true, generalError = null)

            val n = name.trim()
            val e = email.trim().lowercase()
            val p = pass.trim()

            try {
                val result = authRepo.register(
                    email = e,
                    password = p,
                    nombre = n
                )

                result.fold(
                    onSuccess = {
                        // Registro ok
                        ui = RegisterUi(done = true)
                        onSuccess()
                    },
                    onFailure = { ex ->
                        val message = mapExceptionToMessage(ex)
                        ui = ui.copy(
                            loading = false,
                            generalError = message,
                            done = false
                        )
                    }
                )

            } catch (ex: Exception) {
                val message = mapExceptionToMessage(ex)
                ui = ui.copy(
                    loading = false,
                    generalError = message,
                    done = false
                )
            }
        }
    }

    private fun mapExceptionToMessage(e: Throwable): String {
        return when (e) {
            is IOException -> {
                "No se pudo conectar. Revisa tu conexión a internet."
            }

            is HttpException -> {
                when (e.code()) {
                    400 -> "Datos de registro inválidos."
                    409 -> "Ya existe una cuenta registrada con este correo."
                    500 -> "El servidor está teniendo problemas. Intenta más tarde."
                    else -> "Ocurrió un error (${e.code()}). Intenta nuevamente."
                }
            }

            else -> {
                "Ha ocurrido un error inesperado. Intenta nuevamente."
            }
        }
    }
}
