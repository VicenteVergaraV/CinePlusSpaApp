package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val user: UserDto,
    @SerializedName("access_token")
    val accessToken: String
)