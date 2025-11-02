package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoUsers: () -> Unit,
    onGoProfile: () -> Unit,
    onGoCartelera: () -> Unit,
    onGoStore: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido a CinePlus App")
        Button(
            onClick = onGoUsers,
            modifier = Modifier.padding(top = 16.dp)
        ) { Text("Ver Usuarios") }

        Button(
            onClick = onGoProfile,
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Mi Perfil") }

        Button(onClick = onGoCartelera, modifier = Modifier.padding(top = 8.dp)) {
            Text("Cartelera")
        }

        Button(onClick = onGoStore,
            modifier = Modifier.padding(top = 8.dp)) {
            Text("Tienda")
        }
    }
}
