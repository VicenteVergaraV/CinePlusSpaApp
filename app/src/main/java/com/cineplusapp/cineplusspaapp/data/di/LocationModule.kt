// data/di/LocationModule.kt
package com.cineplusapp.cineplusspaapp.data.di


import com.cineplusapp.cineplusspaapp.BuildConfig
import com.cineplusapp.cineplusspaapp.data.location.FakeLocationProvider
import com.cineplusapp.cineplusspaapp.data.location.LocationProvider
import com.cineplusapp.cineplusspaapp.data.location.RealLocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides @Singleton
    fun provideLocationProvider(
        real: RealLocationProvider,
        fake: FakeLocationProvider
    ): LocationProvider = if (BuildConfig.DEBUG) fake else real
    // Cambia a 'real' si quieres probar GPS en debug:
    // ): LocationProvider = real
}