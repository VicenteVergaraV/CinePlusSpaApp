package com.cineplusapp.cineplusspaapp.domain.mappers

import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import com.cineplusapp.cineplusspaapp.domain.model.User

fun UserDto.toUser(): User {
    return User(
        id = this.id.toInt(),
        email = this.email
    )
}
