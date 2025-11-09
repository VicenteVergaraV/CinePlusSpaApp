package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (access: String, refresh: String?) -> Unit, // <-- CAMBIO
    onGoRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val ui = vm.ui

    Column(Modifier.fillMaxSize().padding(15.dp)) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )

        ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 4.dp)) }

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    // feedback local mínimo
                    return@Button
                }
                vm.login(username, password, onLoginSuccess)
            },
            enabled = !ui.loading,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text(if (ui.loading) "Ingresando..." else "Ingresar")
        }

        TextButton(onClick = onGoRegister, modifier = Modifier.padding(top = 4.dp)) {
            Text("Crear cuenta")
        }
    }
}