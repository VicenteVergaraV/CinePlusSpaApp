package com.cineplusapp.cineplusspaapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cineplusapp.cineplusspaapp.data.local.dao.MovieDao
import com.cineplusapp.cineplusspaapp.data.local.dao.ProductDao
import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao
import com.cineplusapp.cineplusspaapp.data.local.dao.UserDao
import com.cineplusapp.cineplusspaapp.data.local.entity.MovieEntity
import com.cineplusapp.cineplusspaapp.data.local.entity.ProductEntity
import com.cineplusapp.cineplusspaapp.data.local.entity.Ticket
import com.cineplusapp.cineplusspaapp.data.local.entity.User

@Database(
    entities = [User::class, Ticket::class, MovieEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ticketDao(): TicketDao

    abstract fun movieDao(): MovieDao

    abstract fun productDao(): ProductDao
}