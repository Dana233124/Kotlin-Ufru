package com.example.urfu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.urfu.ui.MovieDetailsScreen
import com.example.urfu.ui.MoviesScreen
import com.example.urfu.ui.FavoritesScreen
import com.example.urfu.ui.components.BottomNavigationBar
import com.example.urfu.model.Movie

@Composable
fun NavGraph() {
    val navController: NavHostController = rememberNavController()

    val movies = remember {
        mutableStateListOf(
            Movie(
                id = "1",
                title = "Inception",
                director = "Christopher Nolan",
                studio = "Warner Bros.",
                budget = 160_000_000,
                revenue = 829_000_000,
                actors = listOf("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page"),
                description = "A mind-bending thriller about dreams within dreams.",
                year = 2010,
                country = "USA",
                genre = "Sci-Fi, Thriller",
                screenplay = "Christopher Nolan"
            ),
            Movie(
                id = "2",
                title = "Interstellar",
                director = "Christopher Nolan",
                studio = "Paramount Pictures",
                budget = 165_000_000,
                revenue = 677_000_000,
                actors = listOf("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
                description = "Journey through space and time to save humanity.",
                year = 2014,
                country = "USA",
                genre = "Sci-Fi, Drama",
                screenplay = "Jonathan Nolan, Christopher Nolan"
            ),
            Movie(
                id = "3",
                title = "The Dark Knight",
                director = "Christopher Nolan",
                studio = "Warner Bros.",
                budget = 185_000_000,
                revenue = 1_000_000_000,
                actors = listOf("Christian Bale", "Heath Ledger", "Aaron Eckhart"),
                description = "Batman faces the Joker in Gotham City.",
                year = 2008,
                country = "USA",
                genre = "Action, Crime, Drama",
                screenplay = "Jonathan Nolan, Christopher Nolan, David S. Goyer"
            ),
            Movie(
                id = "4",
                title = "Tenet",
                director = "Christopher Nolan",
                studio = "Warner Bros.",
                budget = 200_000_000,
                revenue = 365_000_000,
                actors = listOf("John David Washington", "Robert Pattinson", "Elizabeth Debicki"),
                description = "Espionage thriller with time inversion.",
                year = 2020,
                country = "USA, UK",
                genre = "Sci-Fi, Action",
                screenplay = "Christopher Nolan"
            ),
            Movie(
                id = "5",
                title = "Dune",
                director = "Denis Villeneuve",
                studio = "Legendary Pictures",
                budget = 165_000_000,
                revenue = 402_000_000,
                actors = listOf("Timothée Chalamet", "Rebecca Ferguson", "Oscar Isaac"),
                description = "Epic sci-fi saga based on Frank Herbert's novel.",
                year = 2021,
                country = "USA",
                genre = "Sci-Fi, Adventure",
                screenplay = "Jon Spaihts, Denis Villeneuve, Eric Roth"
            ),
            Movie(
                id = "6",
                title = "The Matrix",
                director = "Lana Wachowski, Lilly Wachowski",
                studio = "Warner Bros.",
                budget = 63_000_000,
                revenue = 466_000_000,
                actors = listOf("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                description = "Neo discovers the truth about the Matrix.",
                year = 1999,
                country = "USA",
                genre = "Sci-Fi, Action",
                screenplay = "Lana Wachowski, Lilly Wachowski"
            )
        )
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "movies",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("movies") { MoviesScreen(navController, movies) }
            composable("details/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")
                MovieDetailsScreen(movieId, movies)
            }
            composable("favorites") { FavoritesScreen(navController, movies) }
        }
    }
}