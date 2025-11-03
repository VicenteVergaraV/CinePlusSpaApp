package com.cineplusapp.cineplusspaapp.data.di

import android.content.Context // Importación correcta del Context de Android
import androidx.room.Room
import com.cineplusapp.cineplusspaapp.data.local.dao.CartItemDao
import com.cineplusapp.cineplusspaapp.data.local.dao.FavoriteMovieDao
import com.cineplusapp.cineplusspaapp.data.local.dao.MovieDao
import com.cineplusapp.cineplusspaapp.data.local.dao.ProductDao
import com.cineplusapp.cineplusspaapp.data.local.dao.TicketDao
import com.cineplusapp.cineplusspaapp.data.local.dao.UserDao // Importación faltante
import com.cineplusapp.cineplusspaapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee la instancia Singleton de AppDatabase.
     * Usa @ApplicationContext para obtener el Context de Android necesario para Room.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cineplus_db"
        ).fallbackToDestructiveMigration() // Manejo temporal de migraciones para desarrollo
            .build()
    }

    /**
     * Provee el DAO para operaciones de usuario.
     */
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    /**
     * Provee el DAO para operaciones de tickets.
     */
    @Provides
    fun provideTicketDao(appDatabase: AppDatabase): TicketDao {
        return appDatabase.ticketDao()
    }


    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    /* Dao del Carrito y la pelicula favorita*/
    @Provides
    fun provideCartItemDao(appDatabase: AppDatabase): CartItemDao {
        return appDatabase.cartItemDao()
    }

    @Provides
    fun provideFavoriteMovieDao(appDatabase: AppDatabase): FavoriteMovieDao {
        return appDatabase.favoriteMovieDao()
    }
}