// ui/screens/MovieDetailScreen.kt
package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    vm: MovieViewModel = hiltViewModel()
) {
    val movie by vm.byId(movieId).collectAsState()

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(movie?.title ?: "Cargando…")
        Spacer(Modifier.height(8.dp))
        // Usa synopsis si existe; si no, muestra los géneros (lista → string)
        Text(movie?.synopsis?.takeIf { it.isNotBlank() }
            ?: movie?.genres?.joinToString(", ")
            ?: "—")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
