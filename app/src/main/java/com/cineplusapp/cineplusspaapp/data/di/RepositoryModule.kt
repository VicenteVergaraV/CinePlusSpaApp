package com.cineplusapp.cineplusspaapp.data.di

import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import com.cineplusapp.cineplusspaapp.repository.CartRepository
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
import com.cineplusapp.cineplusspaapp.repository.impl.AuthRepositoryImpl
import com.cineplusapp.cineplusspaapp.repository.impl.CartRepositoryRoom
import com.cineplusapp.cineplusspaapp.repository.impl.MovieRepositoryLocal
import com.cineplusapp.cineplusspaapp.repository.impl.ProductRepositoryLocal
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryRoom): CartRepository

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryLocal): MovieRepository

    @Binds @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryLocal): ProductRepository
}