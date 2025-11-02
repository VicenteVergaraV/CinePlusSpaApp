package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@Composable
fun CarteleraScreen(
    onMovieClick: (Int) -> Unit,
    vm: MovieViewModel = hiltViewModel()
) {
    val movies by vm.movies.collectAsState()
    LaunchedEffect(Unit) { vm.seed() }

    LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), modifier = Modifier.fillMaxSize().padding(12.dp)) {
        items(movies) { m ->
            Card(
                modifier = Modifier.padding(8.dp).clickable { onMovieClick(m.id) },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(8.dp)) {
                    if (m.posterUrl != null) {
                        Image(rememberAsyncImagePainter(m.posterUrl), null,
                            Modifier.fillMaxWidth().height(180.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(m.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                    Text("${m.durationMin} min · ${m.rating}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
