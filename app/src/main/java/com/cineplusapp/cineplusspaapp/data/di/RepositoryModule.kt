package com.cineplusapp.cineplusspaapp.di

import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import com.cineplusapp.cineplusspaapp.repository.ProductRepository
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
    @Binds @Singleton abstract fun bindMovieRepository(impl: MovieRepositoryLocal): MovieRepository
    @Binds @Singleton abstract fun bindProductRepository(impl: ProductRepositoryLocal): ProductRepository
}
