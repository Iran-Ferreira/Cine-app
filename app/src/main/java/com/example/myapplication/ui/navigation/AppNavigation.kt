package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.FavoritesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.MovieDetailScreen
import com.example.myapplication.ui.screens.MovieListScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMovieList = {
                    navController.navigate(Screen.MovieList.route)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(Screen.MovieList.route) {
            MovieListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMovieSelected = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                }
            )
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailScreen(
                movieId = movieId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMovieSelected = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                }
            )
        }
    }
}
