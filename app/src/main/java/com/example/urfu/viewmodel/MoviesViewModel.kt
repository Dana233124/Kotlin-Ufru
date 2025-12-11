package com.example.urfu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urfu.GetMoviesUseCase
import com.example.urfu.SearchMoviesUseCase
import com.example.urfu.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MoviesState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class MoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesState(isLoading = true))
    val state: StateFlow<MoviesState> = _state

    init {
        loadMovies()
    }

    fun loadMovies() {
        _state.value = MoviesState(isLoading = true)
        viewModelScope.launch {
            try {
                val movies = getMoviesUseCase()
                _state.value = MoviesState(movies = movies)
            } catch (e: Exception) {
                _state.value = MoviesState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun search(text: String) {
        if (text.isBlank()) {
            loadMovies()
            return
        }

        _state.value = MoviesState(isLoading = true)
        viewModelScope.launch {
            try {
                val movies = searchMoviesUseCase(text)
                _state.value = MoviesState(movies = movies)
            } catch (e: Exception) {
                _state.value = MoviesState(error = e.message ?: "Search error")
            }
        }
    }

    fun toggleFavorite(movieId: String) {
        val updated = _state.value.movies.map {
            if (it.id == movieId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _state.value = _state.value.copy(movies = updated)
    }
}
