package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (access: String) -> Unit,
    onGoRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(uiState.authTokens) {
        uiState.authTokens?.let {
            onLoginSuccess(it.access)
        }
    }

    Column(Modifier.fillMaxSize().padding(15.dp)) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            isError = uiState.error != null
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            isError = uiState.error != null
        )

        uiState.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 4.dp)) }

        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    vm.login(username, password)
                }
            },
            enabled = !uiState.loading,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text(if (uiState.loading) "Ingresando..." else "Ingresar")
        }

        TextButton(onClick = onGoRegister, enabled = !uiState.loading, modifier = Modifier.padding(top = 4.dp)) {
            Text("Crear cuenta")
        }
    }
}