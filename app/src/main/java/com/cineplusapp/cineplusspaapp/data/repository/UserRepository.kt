package com.cineplusapp.cineplusspaapp.data.repository

import android.content.Context
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.RetrofitClient
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto

class UserRepository(context: Context) {
    //Creamdo la instancia del API Service
    private val apiService: ApiService = RetrofitClient
        .create(context)
        .create(ApiService::class.java)

    // Obtencion de un usuario de la API
    suspend fun fetchUser(id: Int = 1): Result<UserDto> {
        return try {
            // llamar a la API
            val user = apiService.getUserById(id)

            Result.success(user)

        } catch (e: Exception) {
            // En el caso de que algo falle (sin conexion, timeout, etc)
            Result.failure(e)
        }
    }
}