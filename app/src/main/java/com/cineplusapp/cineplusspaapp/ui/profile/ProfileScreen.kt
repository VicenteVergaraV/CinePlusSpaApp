package com.cineplusapp.cineplusspaapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cineplusapp.cineplusspaapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val user by vm.user.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadUserProfile()
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        user?.let {
            Image(
                painter = rememberAsyncImagePainter(it.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
            Text("Nombre: ${it.nombre ?: "No especificado"}")
            Text("Email: ${it.email}")
            Text("Teléfono: ${it.telefono ?: "No especificado"}")
        }

        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = onEdit) { Text("Editar") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onBack) { Text("Volver") }
        }
    }
}
