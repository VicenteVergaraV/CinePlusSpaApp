// viewmodel/MovieViewModel.kt
package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    var uiState by mutableStateOf(MoviesUiState())
        private set

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)
            try {
                repo.list().collect { movies ->
                    uiState = uiState.copy(
                        loading = false,
                        movies = movies,
                        error = null
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = e.message ?: "Error al cargar películas"
                )
            }
        }
    }

    fun byId(id: Int): Flow<MovieUi?> = repo.byId(id)
}

data class MoviesUiState(
    val loading: Boolean = false,
    val movies: List<MovieUi> = emptyList(),
    val error: String? = null
)
