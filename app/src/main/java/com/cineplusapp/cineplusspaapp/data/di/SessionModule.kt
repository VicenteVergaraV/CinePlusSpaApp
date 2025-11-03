package com.cineplusapp.cineplusspaapp.data.di

import android.content.Context
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides @Singleton
    fun provideSessionManager(@ApplicationContext ctx: Context) = SessionManager(ctx)
}
