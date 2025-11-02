package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "tickets")
data class Ticket(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val movieTitle: String,
    val showtime: Long,
    val seats: String,
    val totalAmount: Double
)
