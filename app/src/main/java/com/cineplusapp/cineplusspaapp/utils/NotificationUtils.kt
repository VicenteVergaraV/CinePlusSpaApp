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
import com.cineplusapp.cineplusspaapp.data.local.entity.Ticket
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

    // Función que debe llamarse al inicio de la aplicación (e.g., en AppDependencies.kt)
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notificaciones para confirmar y recordar funciones de cine."
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendPurchaseNotification(context: Context, ticket: Ticket) {
        // Verificar permiso de POST_NOTIFICATIONS para API 33+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // En un entorno Compose, esto debe solicitar el permiso. Aquí solo logeamos.
            return
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val showtimeString = timeFormat.format(Date(ticket.showtime))

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today) // Ícono genérico
            .setContentTitle("¡Compra Exitosa!")
            .setContentText("Tu entrada para '${ticket.movieTitle}' a las $showtimeString ha sido confirmada.")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Detalles: ${ticket.movieTitle} | Hora: $showtimeString | Asientos: ${ticket.seats} | Total: $${String.format("%.2f", ticket.totalAmount)}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId debe ser único para cada ticket
            notify(NOTIFICATION_ID_BASE + ticket.id, builder.build())
        }
    }
}