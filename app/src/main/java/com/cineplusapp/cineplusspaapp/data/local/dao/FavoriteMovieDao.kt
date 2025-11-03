package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cineplusapp.cineplusspaapp.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie WHERE userId = :userId")
    fun favorites(userId: Int): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteMovieEntity): Long

    @Query("SELECT * FROM favorite_movie WHERE synced = 0")
    suspend fun findPending(): List<FavoriteMovieEntity>

    @Query("UPDATE favorite_movie SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Long)
}