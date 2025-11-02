package com.cineplusapp.cineplusspaapp.data.di

import androidx.room.Room
import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao
import com.cineplusapp.cineplusspaapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.openjdk.tools.javac.util.Context
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cineplus_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideTicketDao(appDatabase: AppDatabase): TicketDao {
        return appDatabase.ticketDao()
    }
}