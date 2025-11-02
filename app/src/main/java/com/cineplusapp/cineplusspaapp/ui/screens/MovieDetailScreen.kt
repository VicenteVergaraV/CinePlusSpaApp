package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    vm: MovieViewModel = hiltViewModel()
) {
    val movie by vm.movie(movieId).collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        movie?.let { m ->
            if (m.posterUrl != null) {
                Image(rememberAsyncImagePainter(m.posterUrl), null, Modifier.fillMaxWidth().height(240.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(m.title, style = MaterialTheme.typography.headlineSmall)
            Text("${m.durationMin} min · ${m.rating}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(m.synopsis)
            Spacer(Modifier.height(12.dp))
            Text("Funciones:", style = MaterialTheme.typography.titleMedium)
            m.showtimes.forEach { s -> Text("• $s") }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Volver") }
        } ?: Text("Cargando…")
    }
}
