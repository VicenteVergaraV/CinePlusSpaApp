package com.cineplusapp.cineplusspaapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditProfileScreen(onSaved: () -> Unit) {
    var name by remember { mutableStateOf("Mr Junior") }
    var email by remember { mutableStateOf("junior@example.com") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Editar Perfil", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

        Button(onClick = {
            // TODO: llamar a tu repositorio para persistir cambios (Room/Api)
            onSaved()
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Guardar")
        }
    }
}
