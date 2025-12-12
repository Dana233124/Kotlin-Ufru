package com.example.urfu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.urfu.cache.FilterBadgeCache
import com.example.urfu.model.Movie

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MoviesScreen(
    navController: NavController,
    movies: List<Movie>,
    onSearch: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    cache: FilterBadgeCache
) {
    var query by remember { mutableStateOf("") }
    val showBadge by cache.showBadge.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                actions = {
                    IconButton(onClick = onFilterClick) {
                        BadgedBox(
                            badge = {
                                if (showBadge) {
                                    Badge { Text("!") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                    }
                }
            )
        }
    ) { padd ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padd)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { onSearch(query) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (movies.isEmpty()) {
                Text("No results", modifier = Modifier.padding(16.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("details/${movie.id}") }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(movie.title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "${movie.year} • ${movie.genre} • Rating: ${movie.rating ?: "N/A"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(movie.description, maxLines = 3, style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Director: ${movie.director}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                IconButton(onClick = { onFavoriteClick(movie.id) }) {
                                    Icon(
                                        imageVector = if (movie.isFavorite)
                                            Icons.Filled.Favorite
                                        else
                                            Icons.Filled.FavoriteBorder,
                                        contentDescription = "Favorite"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
