package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val type: String           // "Cabritas" | "Bebestibles" | "Promociones"
)
