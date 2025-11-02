package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun UserDetailScreen(userId: Int, onBack: () -> Unit) {
    // TODO: reemplazar por ViewModel + ApiService.getUserById(userId)
    val avatar = "https://i.pravatar.cc/256?img=${userId.coerceAtLeast(1)}"
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Detalle Usuario #$userId", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Image(painter = rememberAsyncImagePainter(avatar), contentDescription = null, modifier = Modifier.size(128.dp))
        Spacer(Modifier.height(16.dp))
        Text("Nombre: Usuario $userId")
        Text("Email: usuario$userId@example.com")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
