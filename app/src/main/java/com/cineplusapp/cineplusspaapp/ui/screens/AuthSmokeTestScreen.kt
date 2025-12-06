package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.AuthViewModel

@Composable
fun AuthSmokeTestScreen(vm: AuthViewModel = hiltViewModel()) {
    val status by vm.status.collectAsState()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Smoke Test Auth/JWT")
            Spacer(Modifier.height(8.dp))
            Text(status)
        }
    }
}
