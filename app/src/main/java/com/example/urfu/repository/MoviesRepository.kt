package com.example.urfu.repository

import com.example.urfu.client.ApiClient
import com.example.urfu.model.Movie
import com.example.urfu.service.ApiTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {

    private val api = ApiClient.api

    private fun map(apiTitle: ApiTitle): Movie {
        return Movie(
            id = apiTitle.id,
            title = apiTitle.primaryTitle ?: apiTitle.originalTitle ?: "Unknown",
            director = "Unknown",
            studio = "Unknown",
            budget = 0,
            revenue = 0,
            actors = emptyList(),
            description = apiTitle.plot ?: "",
            year = apiTitle.startYear ?: 0,
            country = "Unknown",
            genre = apiTitle.genres?.joinToString(", ") ?: "Unknown",
            screenplay = "",
            isFavorite = false,
            rating = apiTitle.rating?.aggregateRating?.toFloat()
        )
    }

    suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        api.getTitles().titles.map { map(it) }
    }

    suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        api.getTitles().titles
            .filter { it.primaryTitle?.contains(query, ignoreCase = true) == true }
            .map { map(it) }
    }

    suspend fun getMovieById(id: String): Movie? = withContext(Dispatchers.IO) {
        try {
            val title = api.getTitles().titles.find { it.id == id }
            title?.let { map(it) }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun getMoviesWithFilters(
        genre: String?,
        minRating: Float?,
        title: String?
    ): List<Movie> = withContext(Dispatchers.IO) {
        val response = api.getTitles(
            genres = genre?.let { listOf(it) },
            minAggregateRating = minRating
        )

        val filtered = response.titles.map { map(it) }
            .filter { movie ->
                (minRating == null || (movie.rating ?: 0f) >= minRating) &&
                        (title.isNullOrBlank() || movie.title.contains(title, ignoreCase = true))
            }

        filtered
    }
}
