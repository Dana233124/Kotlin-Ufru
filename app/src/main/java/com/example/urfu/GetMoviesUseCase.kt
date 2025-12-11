package com.example.urfu

import com.example.urfu.repository.MovieRepository

class GetMoviesUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke() = repo.getMovies()
}
