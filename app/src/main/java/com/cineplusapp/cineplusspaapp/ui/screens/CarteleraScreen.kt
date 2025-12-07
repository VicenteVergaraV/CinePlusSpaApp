package com.cineplusapp.cineplusspaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import com.cineplusapp.cineplusspaapp.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarteleraScreen(
    vm: MovieViewModel,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val state = vm.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cartelera") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when {
                state.loading -> {
                    LoadingView()
                }
                state.error != null -> {
                    ErrorView(
                        message = state.error,
                        onRetry = { vm.loadMovies() }
                    )
                }
                else -> {
                    MoviesList(
                        movies = state.movies,
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}

/* ---------- Helpers UI básicos ---------- */

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun MoviesList(
    movies: List<MovieUi>,
    onMovieClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onClick = {
                    // si tu MovieUi tiene id String, conviene parsearlo a Int
                    // o cambiar el tipo del callback a (String) -> Unit
                    val idInt = movie.id.toIntOrNull() ?: -1
                    if (idInt != -1) onMovieClick(idInt)
                }
            )
        }
    }
}

@Composable
fun MovieItem(
    movie: MovieUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = movie.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            if (!movie.genero.isNullOrBlank()) {
                Text(
                    text = movie.genero,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (!movie.sinopsis.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.sinopsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}