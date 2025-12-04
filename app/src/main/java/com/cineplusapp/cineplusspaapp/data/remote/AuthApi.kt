package com.cineplusapp.cineplusspaapp.data.remote

import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RefreshRequest(val refreshToken: String)
data class RefreshResponse(val token: String, val refreshToken: String?)

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("auth/profile")
    suspend fun getMyAuthProfile(): UserDto
}
