package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@Composable
fun CarteleraScreen(
    onMovieClick: (Int) -> Unit,
    vm: MovieViewModel = hiltViewModel(),
    onBack:() -> Unit
) {
    val movies by vm.movies.collectAsState()
    LaunchedEffect(Unit) { vm.seed() }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cartelera", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        movies.forEach { m ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onMovieClick(m.numericId) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(m.titulo, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        if (movies.isEmpty()) {
            Text("Sin películas cargadas.")
        }
    }
}