package com.cineplusapp.cineplusspaapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.local.entity.Ticket
import com.cineplusapp.cineplusspaapp.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val pdfGenerator: PdfGenerator,
    private val notification: NotificationUtils,
    @ApplicationContext private val context: Context
): ViewModel() {

    fun processPurcharse(
        userId: String,
        movieTitle: String,
        seats: String,
        amount: Double
    ) {
        viewModelScope.launch {
            val newTicket = Ticket(
                userId = userId,
                movieTitle = movieTitle,
                showtime = Date().time + (60 * 60 * 1000), // ejemplo de una funcion en 1 hora
                seats = seats,
                totalAmount = amount
            )

            ticketRepository.saveTicket(newTicket)

            pdfGenerator.generateTicketPdf(context, newTicket)

            notificationUtils.sendPurchaseNotification(context, newTicket)
        }
    }
}