package com.example.urfu.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.urfu.model.Movie

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailsScreen(movieId: String?, movies: List<Movie>) {
    val movie = movies.find { it.id == movieId }

    movie?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineLarge
            )

            Divider()

            InfoRow(label = "Year", value = it.year.toString())
            InfoRow(label = "Country", value = it.country)
            InfoRow(label = "Genre", value = it.genre)
            InfoRow(label = "Director", value = it.director)
            InfoRow(label = "Screenplay", value = it.screenplay)
            InfoRow(label = "Studio", value = it.studio)
            InfoRow(label = "Budget", value = "$${it.budget}")
            InfoRow(label = "Revenue", value = "$${it.revenue}")

            Divider()

            Text("Actors:", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                it.actors.forEach { actor ->
                    AssistChip(
                        onClick = { /* можно добавить действие */ },
                        label = { Text(actor) }
                    )
                }
            }

            Divider()

            Text("Description:", style = MaterialTheme.typography.titleMedium)
            Text(it.description, style = MaterialTheme.typography.bodyMedium)
        }
    } ?: Text("Movie not found", modifier = Modifier.padding(16.dp))
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}