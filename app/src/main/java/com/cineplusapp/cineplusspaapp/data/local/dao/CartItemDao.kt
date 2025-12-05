package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cineplusapp.cineplusspaapp.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE synced = 0")
    fun findPendingFlow(): Flow<List<CartItemEntity>>

    @Query("UPDATE cart_items SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Long)
}
