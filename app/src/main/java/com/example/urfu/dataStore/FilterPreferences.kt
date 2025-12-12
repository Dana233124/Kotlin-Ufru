package com.example.urfu.datastore

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.filterDataStore by preferencesDataStore("filters")

object FilterKeys {
    val GENRE = stringPreferencesKey("genre")
    val TITLE = stringPreferencesKey("title")
    val MIN_RATING = floatPreferencesKey("min_rating")
}

suspend fun saveFilters(context: Context, genre: String?, title: String?, minRating: Float?) {
    context.filterDataStore.edit { prefs ->
        prefs[FilterKeys.GENRE] = genre ?: ""
        prefs[FilterKeys.TITLE] = title ?: ""
        if (minRating != null) prefs[FilterKeys.MIN_RATING] = minRating
    }
}

fun getFilters(context: Context): Flow<Triple<String?, String?, Float?>> =
    context.filterDataStore.data.map { prefs ->
        Triple(
            prefs[FilterKeys.GENRE].takeIf { !it.isNullOrBlank() },
            prefs[FilterKeys.TITLE].takeIf { !it.isNullOrBlank() },
            prefs[FilterKeys.MIN_RATING]
        )
    }
