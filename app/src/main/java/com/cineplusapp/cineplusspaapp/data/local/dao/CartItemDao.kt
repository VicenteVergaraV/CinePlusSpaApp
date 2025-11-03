package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cineplusapp.cineplusspaapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Upsert
    suspend fun upsert(item: CartItemEntity): Long

    @Query("SELECT * FROM cart_item WHERE userId = :userId ORDER BY updatedAt DESC")
    fun itemsByUser(userId: Int): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_item WHERE synced = 0")
    suspend fun findPending(): List<CartItemEntity>

    // Versión Flow (por si te sirve en algún punto)
    @Query("SELECT * FROM cart_item WHERE synced = 0")
    fun findPendingFlow(): Flow<List<CartItemEntity>>

    @Query("UPDATE cart_item SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Long)
}
