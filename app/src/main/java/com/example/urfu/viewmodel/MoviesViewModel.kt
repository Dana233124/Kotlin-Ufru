package com.example.urfu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urfu.datastore.getFilters
import com.example.urfu.model.Movie
import com.example.urfu.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoviesState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MoviesViewModel(
    private val repository: MovieRepository,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesState())
    val state: StateFlow<MoviesState> = _state.asStateFlow()

    init {
        // Загружаем сохранённые фильтры при старте
        viewModelScope.launch {
            getFilters(context).collect { (genre, title, rating) ->
                loadMoviesWithFilters(genre, rating, title)
            }
        }
    }

    fun loadMoviesWithFilters(genre: String?, rating: Float?, title: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val movies = repository.getMoviesWithFilters(genre, rating, title)
                _state.value = MoviesState(movies = movies)
            } catch (e: Exception) {
                _state.value = MoviesState(error = e.message)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val movies = repository.searchMovies(query)
                _state.value = MoviesState(movies = movies)
            } catch (e: Exception) {
                _state.value = MoviesState(error = e.message)
            }
        }
    }

    fun toggleFavorite(id: String) {
        val updated = _state.value.movies.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
        }
        _state.value = _state.value.copy(movies = updated)
    }
}
