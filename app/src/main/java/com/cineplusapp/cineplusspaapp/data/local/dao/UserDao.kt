package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cineplusapp.cineplusspaapp.data.local.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la entidad User.
 * Define los métodos para interactuar con la tabla de usuarios en la base de datos Room.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario o reemplaza un usuario existente si hay conflicto.
     * Se usa para registrar o para guardar el usuario después de iniciar sesión (manejo de sesión).
     *
     * @param user La entidad User a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /**
     * Recupera el usuario actualmente autenticado.
     * @return Un Flow que emite la lista de usuarios. Idealmente, solo contendrá uno.
     */
    @Query("SELECT * FROM user LIMIT 1")
    fun getLoggedInUser(): Flow<List<User>>

    /**
     * Limpia todos los registros de la tabla de usuarios.
     * Se utiliza principalmente al cerrar la sesión (Logout).
     */
    @Query("DELETE FROM user")
    suspend fun clearUserTable()
}
