package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Ticket local
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String
)