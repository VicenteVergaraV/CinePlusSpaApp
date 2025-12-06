package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.viewmodel.AuthViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val status by viewModel.status.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navegar cuando el estado indique éxito
    LaunchedEffect(status) {
        if (status.startsWith("Sesión activa:")) {
            onLoginSuccess()
        }
    }

    // UI de ejemplo
    Column {
        Text(text = status)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }
        )

        Button(onClick = { viewModel.login(email, password) }) {
            Text("Iniciar sesión")
        }

        TextButton(onClick = onGoRegister) {
            Text("Registrarse")
        }
    }
}
