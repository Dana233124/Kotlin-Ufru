package com.example.urfu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.urfu.cache.FilterBadgeCache
import com.example.urfu.datastore.getFilters
import com.example.urfu.datastore.saveFilters
import kotlinx.coroutines.launch

@Composable
fun FilterScreen(
    onApplyFilters: (String?, Float?, String?) -> Unit,
    onBack: () -> Unit,
    cache: FilterBadgeCache
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var genre by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        getFilters(context).collect { (savedGenre, savedTitle, savedRating) ->
            genre = savedGenre ?: ""
            title = savedTitle ?: ""
            rating = savedRating?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Min Rating") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Button(onClick = {
                val ratingValue = rating.toFloatOrNull()
                coroutineScope.launch {
                    saveFilters(context, genre.ifBlank { null }, title.ifBlank { null }, ratingValue)
                }
                cache.setBadgeVisible(true)
                onApplyFilters(genre.ifBlank { null }, ratingValue, title.ifBlank { null })
            }) {
                Text("Apply")
            }
        }
    }
}

