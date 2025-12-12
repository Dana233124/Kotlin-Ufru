package com.example.urfu.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.urfu.cache.FilterBadgeCache
import com.example.urfu.repository.MovieRepository
import com.example.urfu.ui.FavoritesScreen
import com.example.urfu.ui.FilterScreen
import com.example.urfu.ui.MovieDetailsScreen
import com.example.urfu.ui.MoviesScreen
import com.example.urfu.ui.components.BottomNavigationBar
import com.example.urfu.viewmodel.MoviesViewModel
import com.example.urfu.viewmodel.MoviesViewModelFactory

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val repo = remember { MovieRepository() }
    val vm: MoviesViewModel = viewModel(
        factory = MoviesViewModelFactory(repo, context)
    )

    val state by vm.state.collectAsState()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // 🔥 общий кэш для бейджа
    val cache = remember { FilterBadgeCache() }

    Scaffold(
        bottomBar = {
            if (currentRoute != "filter") {
                BottomNavigationBar(navController)
            }
        }
    ) { padd ->
        Box(modifier = Modifier.padding(padd)) {
            when {
                state.isLoading -> {
                    Text("Loading...", modifier = Modifier.padding(16.dp).align(Alignment.TopStart))
                }
                state.error != null -> {
                    Text("Error: ${state.error}", modifier = Modifier.padding(16.dp).align(Alignment.TopStart))
                }
                else -> {
                    NavHost(navController = navController, startDestination = "movies") {
                        composable("movies") {
                            MoviesScreen(
                                navController = navController,
                                movies = state.movies,
                                onSearch = vm::search,
                                onFavoriteClick = vm::toggleFavorite,
                                onFilterClick = { navController.navigate("filter") },
                                cache = cache
                            )
                        }
                        composable("details/{id}") { back ->
                            val id = back.arguments?.getString("id")
                            MovieDetailsScreen(id, state.movies)
                        }
                        composable("favorites") {
                            FavoritesScreen(navController, state.movies)
                        }
                        composable("filter") {
                            FilterScreen(
                                onApplyFilters = { genre, rating, title ->
                                    vm.loadMoviesWithFilters(genre, rating, title)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                cache = cache
                            )
                        }
                    }
                }
            }
        }
    }
}

