package com.example.myapplication.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MovieList : Screen("movie_list")
    object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail/$movieId"
    }
    object Favorites : Screen("favorites")
}
