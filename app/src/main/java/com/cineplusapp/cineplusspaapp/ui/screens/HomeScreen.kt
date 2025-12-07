package com.cineplusapp.cineplusspaapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto

@Composable
fun HomeScreen(
    user: UserDto?,
    onGoUsers: () -> Unit,
    onGoProfile: () -> Unit,
    onGoCartelera: () -> Unit,
    onGoStore: () -> Unit,
    onGoNearby: () -> Unit,           // ← tipo correcto
    onLogout: (() -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            // Temporary text for debugging
            Text("User Role: ${user?.role ?: "null"}")
            Spacer(Modifier.height(12.dp))
        }

        item {
            Text("Bienvenido a CinePlus App")
            Spacer(Modifier.height(12.dp))
        }

        if (user?.role?.equals("admin", ignoreCase = true) == true) {
            Log.d("HomeScreen", "User is admin, showing Ver Usuarios button")
            item {
                Button(onClick = onGoUsers) { Text("Ver Usuarios") }
                Spacer(Modifier.height(8.dp))
            }
        }

        item {
            Button(onClick = onGoProfile) { Text("Mi Perfil") }
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(onClick = onGoCartelera) { Text("Cartelera") }
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(onClick = onGoStore) { Text("Tienda") }
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(onClick = onGoNearby) { Text("Cines cercanos (GPS)") }  // ← botón Nearby
            Spacer(Modifier.height(16.dp))
        }

        if (onLogout != null) {
            item {
                OutlinedButton(onClick = onLogout) { Text("Cerrar sesión") }
            }
        }
    }
}