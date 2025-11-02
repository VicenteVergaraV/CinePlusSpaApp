package com.cineplusapp.cineplusspaapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import com.cineplusapp.cineplusspaapp.ui.navigation.AppNavigation
import com.cineplusapp.cineplusspaapp.ui.theme.CinePlusSPAAPPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPostNotifications =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* manejar si quieres */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPostNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            CinePlusSPAAPPTheme {
                Surface {   AppNavigation()
                }
            }
        }
    }
}