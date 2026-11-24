package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.domain.model.Movie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository = MovieRepository(
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    val favoriteMovies: StateFlow<List<Movie>> = repository.getFavoriteMovies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.removeFavorite(movie)
        }
    }
}
