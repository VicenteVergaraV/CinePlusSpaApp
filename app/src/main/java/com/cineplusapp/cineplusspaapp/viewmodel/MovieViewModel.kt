package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val movies: StateFlow<List<Movie>> =
        repo.movies.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seed() = viewModelScope.launch { repo.seedIfEmpty() }

    fun movie(id: Int): StateFlow<Movie?> =
        repo.byId(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
