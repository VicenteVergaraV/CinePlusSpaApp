package com.cineplusapp.cineplusspaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.ui.navigation.AppNavigation
import com.cineplusapp.cineplusspaapp.ui.theme.CinePlusSPAAPPTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinePlusSPAAPPTheme {
                AppNavigation(session = sessionManager)
            }
        }
    }
}
