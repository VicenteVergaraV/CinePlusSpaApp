package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUi(
    val loading: Boolean = false,
    val error: String? = null,
    val authTokens: AuthTokens? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUi())
    val uiState: StateFlow<LoginUi> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, authTokens = null) }
            try {
                val result = authRepository.login(email, password)
                result.fold(
                    onSuccess = { authTokens ->
                        sessionManager.saveAuthToken(authTokens.access)
                        _uiState.update { it.copy(loading = false, authTokens = authTokens) }
                    },
                    onFailure = { exception ->
                        _uiState.update { it.copy(loading = false, error = exception.message) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}