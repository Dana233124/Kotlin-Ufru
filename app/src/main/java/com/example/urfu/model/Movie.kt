package com.example.urfu.model

data class Movie(
    val id: String,
    val title: String,
    val director: String,
    val studio: String,
    val budget: Int,
    val revenue: Int,
    val actors: List<String>,
    val description: String,
    val year: Int,
    val country: String,
    val genre: String,
    val screenplay: String,
    val isFavorite: Boolean = false,
    val rating: Float? = null
)