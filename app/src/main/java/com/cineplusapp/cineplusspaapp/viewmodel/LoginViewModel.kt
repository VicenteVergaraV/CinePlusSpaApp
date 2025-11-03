package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {
    var ui by mutableStateOf(LoginUi())
        private set

    fun login(user: String, pass: String, onSuccess: () -> Unit) = viewModelScope.launch {
        ui = ui.copy(loading = true, error = null)
        val r = authRepo.login(user, pass)
        if (r.isSuccess) {
            ui = ui.copy(loading = false)
            onSuccess()
        } else {
            ui = ui.copy(loading = false, error = r.exceptionOrNull()?.message ?: "Error")
        }
    }
}

data class LoginUi(val loading: Boolean = false, val error: String? = null)

