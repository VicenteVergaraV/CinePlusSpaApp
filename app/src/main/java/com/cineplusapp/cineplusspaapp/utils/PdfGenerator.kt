package com.cineplusapp.cineplusspaapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.cineplusapp.cineplusspaapp.data.local.entity.Ticket
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PdfGenerator @Inject constructor() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val pdfTitle = "CinePlus Ticket"

    fun generateTicketPdf(context: Context, ticket: Ticket) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        var yPos = 80f
        val margin = 50f
        val lineHeight = 30f

        paint.color = Color.BLACK
        paint.textSize = 24f
        canvas.drawText(pdfTitle, margin, yPos, paint)
        yPos += lineHeight * 2

        paint.textSize = 18f
        canvas.drawText("PELICULA: ${ticket.movieTitle}", margin, yPos, paint)
        yPos += lineHeight
        canvas.drawText("ASIENTOS: ${ticket.seats}", margin, yPos, paint)
        yPos += lineHeight

        val dateString = dateFormat.format(Date(ticket.showtime))
        canvas.drawText("HORA: ${dateString}", margin, yPos, paint)
        yPos += lineHeight
        canvas.drawText("TOTAL: $${String.format("%.2f", ticket.totalAmount)}", margin, yPos, paint)
        yPos += lineHeight * 2

        paint.textSize = 12f
        canvas.drawText("ID de Ticket: ${ticket.id}", margin, yPos, paint)

        document.finishPage(page)

        try {
            val fileName = "ticket_${ticket.id}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)
            document.writeTo(outputStream)

            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }
}