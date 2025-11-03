package com.cineplusapp.cineplusspaapp.data.di

import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import com.cineplusapp.cineplusspaapp.repository.impl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}