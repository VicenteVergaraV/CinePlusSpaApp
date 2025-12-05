package com.cineplusapp.cineplusspaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cineplusapp.cineplusspaapp.data.local.dao.CartItemDao
import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao
import com.cineplusapp.cineplusspaapp.data.local.dao.UserDao
import com.cineplusapp.cineplusspaapp.data.local.entity.CartItemEntity
import com.cineplusapp.cineplusspaapp.domain.model.Ticket
import com.cineplusapp.cineplusspaapp.domain.model.User

@Database(entities = [User::class, Ticket::class, CartItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ticketDao(): TicketDao
    abstract fun cartItemDao(): CartItemDao
}