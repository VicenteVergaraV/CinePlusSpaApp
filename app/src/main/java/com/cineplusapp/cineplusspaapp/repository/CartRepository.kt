package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addLocal(item: CartItemEntity)
    fun pending(): Flow<List<CartItemEntity>>
    suspend fun markSynced(id: Long)
}
