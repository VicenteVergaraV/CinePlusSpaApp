package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val user: UserDto,
    @SerializedName("access_token")
    val accessToken: String?
)
