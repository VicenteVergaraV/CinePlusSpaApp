package com.cineplusapp.cineplusspaapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cineplusapp.cineplusspaapp.domain.model.Ticket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NotificationUtils @Inject constructor() {

    companion object {
        const val CHANNEL_ID = "CinePlus_Purchase_Channel"
        const val CHANNEL_NAME = "Confirmaciones de Compra"
        const val NOTIFICATION_ID_BASE = 100
    }

    fun sendPurchaseNotification(context: Context, ticket: Ticket) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val showtimeString = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
            .format(Date(ticket.showtime))

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle("¡Compra Exitosa!")
            .setContentText("Tu entrada para '${ticket.movieTitle}' a las $showtimeString ha sido confirmada.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Detalles: ${ticket.movieTitle} | Hora: $showtimeString | Asientos: ${ticket.seats} | Total: $${String.format("%.2f", ticket.totalAmount)}"
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_ID_BASE + ticket.id, builder.build())
    }


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}