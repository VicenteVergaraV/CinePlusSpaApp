package com.cineplusapp.cineplusspaapp

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppDependencies : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    // NO declares getWorkManagerConfiguration() como función *y* propiedad a la vez.
    // Implementa SÓLO esta propiedad:
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Opcional: agenda sync periódico al iniciar la app (si lo quieres global)
        // SyncScheduler.schedulePeriodic(WorkManager.getInstance(this))
    }
}