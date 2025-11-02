package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
        Button(
            onClick = {
                loading = true
                // TODO: reemplazar por tu ViewModel con ApiService.login + guardado token
                // Simulación:
                error = if (username.isBlank() || password.isBlank()) "Completa usuario y contraseña" else null
                loading = false
                if (error == null) onLoginSuccess()
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(if (loading) "Ingresando..." else "Ingresar")
        }

        TextButton(onClick = onGoRegister, modifier = Modifier.padding(top = 8.dp)) {
            Text("Crear cuenta")
        }
    }
}
