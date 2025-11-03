package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cineplusapp.cineplusspaapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY title")
    fun observeAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MovieEntity>)

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun count(): Int

    @Query("""
        SELECT * FROM movies 
        WHERE title LIKE :q OR synopsis LIKE :q 
        ORDER BY title
    """)
    fun observeByQuery(q: String): Flow<List<MovieEntity>>
}
