package com.cineplusapp.cineplusspaapp.ui.profile

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
fun ProfileScreen(onEdit: () -> Unit, onBack: () -> Unit) {
    // TODO: reemplazar por ViewModel + ApiService.getCurrentUser() con token
    val name by remember { mutableStateOf("Mr Junior") }
    val email by remember { mutableStateOf("junior@example.com") }
    val avatar by remember { mutableStateOf("https://i.pravatar.cc/128?img=4") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Image(painter = rememberAsyncImagePainter(avatar), contentDescription = null, modifier = Modifier.size(96.dp))
        Spacer(Modifier.height(16.dp))
        Text("Nombre: $name")
        Text("Email: $email")
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = onEdit) { Text("Editar") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onBack) { Text("Volver") }
        }
    }
}
