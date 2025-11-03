// viewmodel/MovieViewModel.kt
package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val movies: StateFlow<List<Movie>> =
        repo.list().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun byId(id: Int): StateFlow<Movie?> =
        repo.byId(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun seed() = viewModelScope.launch {
        repo.seedIfEmpty()
    }
}