package com.example.urfu

import com.example.urfu.repository.MovieRepository

class SearchMoviesUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(text: String) = repo.searchMovies(text)
}
