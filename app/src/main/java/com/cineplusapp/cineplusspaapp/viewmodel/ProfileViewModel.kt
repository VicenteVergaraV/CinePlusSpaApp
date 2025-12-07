package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.domain.model.FullUserProfile
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<FullUserProfile?>(null)
    val user: StateFlow<FullUserProfile?> = _user.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
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
}