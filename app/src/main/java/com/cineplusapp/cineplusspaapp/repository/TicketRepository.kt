package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao
import com.cineplusapp.cineplusspaapp.domain.model.Ticket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun saveTicket(ticket: Ticket) {
        ticketDao.insertTicket(ticket)
    }

    fun getUserTickets(userId:String): Flow<List<Ticket>> {
        return ticketDao.getTicketsByUserId(userId)
    }

    suspend fun getTicketDetails(ticketId: Int): Ticket? {
        return ticketDao.getTicketById(ticketId)
    }
}