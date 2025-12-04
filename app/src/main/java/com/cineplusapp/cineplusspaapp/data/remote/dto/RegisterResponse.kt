package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val user: UserDto,
    @SerializedName("access")
    val accessToken: String
)
