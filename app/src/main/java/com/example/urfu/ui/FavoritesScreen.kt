package com.example.urfu.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.urfu.model.Movie
@Composable
fun FavoritesScreen(navController: NavController, movies: List<Movie>) {
    val favorites = movies.filter { it.isFavorite }
    if (favorites.isEmpty()) {
        Text("No favorites yet", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(favorites) { movie ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    onClick = { navController.navigate("details/${movie.id}") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(movie.title, style = MaterialTheme.typography.titleLarge)
                        Text(movie.description, style = MaterialTheme.typography.bodyMedium)
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favorite")
                    }
                }
            }
        }
    }
}