// data/local/entity/CartItemEntity.kt
package com.cineplusapp.cineplusspaapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_item",
    indices = [Index("userId")]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userId: Int,
    val productId: Int,
    val qty: Int,
    val price: Double,
    val synced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
