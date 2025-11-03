package com.cineplusapp.cineplusspaapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(session = session)
        }
    }
}