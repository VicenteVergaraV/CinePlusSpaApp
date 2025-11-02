package com.cineplusapp.cineplusspaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cineplusapp.cineplusspaapp.data.local.entity.Ticket
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert
    suspend fun insertTicket(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE userId = :userId ORDER BY showtime DESC")
    fun getTicketsByUserId(userId: String): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE id = :ticketId")
    suspend fun getTicketById(ticketId: Int): Ticket?
}