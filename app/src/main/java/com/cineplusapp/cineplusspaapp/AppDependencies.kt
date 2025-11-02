package com.cineplusapp.cineplusspaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppDependencies : Application() {
    override fun onCreate() {
        super.onCreate()
        com.cineplusapp.cineplusspaapp.utils.NotificationUtils().createNotificationChannel(this)
    }
}