package com.cineplusapp.cineplusspaapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val error: String? = null
)


class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadUser(id: Int = 1) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            val result = repository.fetchUser(id)

            _uiState.value = result.fold(
                onSuccess = { user ->

                    _uiState.value.copy(
                        isLoading = false,
                        userName = user.username,
                        userEmail = user.email ?: "Sin email",
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Error desconocido"
                    )
                }
            )
        }
    }
}