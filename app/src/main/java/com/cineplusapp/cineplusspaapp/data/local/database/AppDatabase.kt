package com.cineplusapp.cineplusspaapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao

@Database(
    entities = [User::class, Ticket::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ticketDao(): TicketDao
}