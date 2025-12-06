package com.cineplusapp.cineplusspaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val role: String
)