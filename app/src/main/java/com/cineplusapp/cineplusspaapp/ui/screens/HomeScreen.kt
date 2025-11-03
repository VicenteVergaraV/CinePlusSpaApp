package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoUsers: () -> Unit,
    onGoProfile: () -> Unit,
    onGoCartelera: () -> Unit,
    onGoStore: () -> Unit,
    onGoNearby: () -> Unit,           // ← tipo correcto
    onLogout: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido a CinePlus App")
        Spacer(Modifier.height(12.dp))

        Button(onClick = onGoUsers) { Text("Ver Usuarios") }
        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoProfile) { Text("Mi Perfil") }
        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoCartelera) { Text("Cartelera") }
        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoStore) { Text("Tienda") }
        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoNearby) { Text("Cines cercanos (GPS)") }  // ← botón Nearby
        Spacer(Modifier.height(16.dp))

        if (onLogout != null) {
            OutlinedButton(onClick = onLogout) { Text("Cerrar sesión") }
        }
    }
}
