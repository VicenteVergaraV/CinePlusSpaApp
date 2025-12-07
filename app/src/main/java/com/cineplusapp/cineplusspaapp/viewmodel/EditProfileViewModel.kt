package com.cineplusapp.cineplusspaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.remote.dto.UpdateUsuarioProfileRequest
import com.cineplusapp.cineplusspaapp.domain.model.FullUserProfile
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<FullUserProfile?>(null)
    val user: StateFlow<FullUserProfile?> = _user.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val authResult = authRepository.getMe()
            val profileResult = authRepository.getMyProfile()

            if (authResult.isSuccess && profileResult.isSuccess) {
                val authUser = authResult.getOrNull()!!
                val profile = profileResult.getOrNull()!!

                _user.value = FullUserProfile(
                    id = authUser.id,
                    email = authUser.email,
                    role = authUser.role,
                    nombre = profile.nombre,
                    telefono = profile.telefono,
                    avatar = profile.avatar
                )
            }
        }
    }

    fun updateUserProfile(nombre: String, telefono: String, avatar: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            val request = UpdateUsuarioProfileRequest(
                nombre = nombre,
                telefono = telefono,
                avatar = avatar
            )
            Log.d("EditProfileViewModel", "Updating profile with: $request")

            authRepository.updateMyProfile(request)
                .onSuccess { updatedProfile ->
                    Log.d("EditProfileViewModel", "Update successful: $updatedProfile")
                    val currentUser = _user.value
                    if (currentUser != null) {
                        _user.value = currentUser.copy(
                            nombre = updatedProfile.nombre,
                            telefono = updatedProfile.telefono,
                            avatar = updatedProfile.avatar
                        )
                        Log.d("EditProfileViewModel", "Local user state updated: ${_user.value}")
                    }
                    onSaved()
                }
                .onFailure { error ->
                    Log.e("EditProfileViewModel", "Update failed", error)
                }
        }
    }
}