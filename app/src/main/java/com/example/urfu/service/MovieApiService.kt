package com.example.urfu.service

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class ApiTitle(
    val id: String,
    val primaryTitle: String?,
    val originalTitle: String?,
    val plot: String?,
    val startYear: Int?,
    val endYear: Int?,
    val runtimeSeconds: Int?,
    val genres: List<String>?,
    val rating: Rating?
)

@Serializable
data class Rating(
    val aggregateRating: Double? = null,
    val voteCount: Int? = null
)

@Serializable
data class PrimaryImage(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class TitlesResponse(
    val titles: List<ApiTitle> = emptyList(),
    val totalCount: Int? = null,
    val nextPageToken: String? = null
)

interface MovieApiService {
    @GET("titles")
    suspend fun getTitles(
        @Query("types") types: List<String>? = null,
        @Query("genres") genres: List<String>? = null,
        @Query("countryCodes") countryCodes: List<String>? = null,
        @Query("languageCodes") languageCodes: List<String>? = null,
        @Query("startYear") startYear: Int? = null,
        @Query("endYear") endYear: Int? = null,
        @Query("minVoteCount") minVoteCount: Int? = null,
        @Query("maxVoteCount") maxVoteCount: Int? = null,
        @Query("minAggregateRating") minAggregateRating: Float? = null,
        @Query("maxAggregateRating") maxAggregateRating: Float? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("pageToken") pageToken: String? = null
    ): TitlesResponse
}
