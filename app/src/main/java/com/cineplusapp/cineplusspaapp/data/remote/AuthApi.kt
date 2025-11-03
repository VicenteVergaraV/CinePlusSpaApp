package com.cineplusapp.cineplusspaapp.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class RefreshRequest(val refreshToken: String)
data class RefreshResponse(val token: String, val refreshToken: String?)

interface AuthApi {
    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): RefreshResponse
}
