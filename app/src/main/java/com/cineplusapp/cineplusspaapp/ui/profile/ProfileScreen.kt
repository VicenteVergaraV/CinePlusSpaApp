package com.cineplusapp.cineplusspaapp.ui.profile

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    // Collects the state from the ViewModel
    val state by viewModel.uiState.collectAsState()

    // Load user data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadUser(1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when {
            // 1. Loading State
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 2. Error State
            state.error != null -> {
                Column (
                    modifier = Modifier.align(Alignment.Center), // <-- CORRECCIÓN: Coma añadida
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌ Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser(1) }) {
                        Text("Reintentar")
                    }
                }
            }

            // 3. Success State
            else -> {
                Column (
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Perfil de Usuario",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Este podría ajustarse con el padding/arrangement

                    Card (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column (modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nombre",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.userName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Card (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column (modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.userEmail,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // Este también

                    Button(onClick = { viewModel.loadUser(1) }) {
                        Text("Refrescar")
                    }
                }
            }
        }
    }
}