package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.remote.TranslationService
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    val repository: MovieRepository = MovieRepository(
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            val foundMovie = repository.getMovieById(movieId)
            if (foundMovie != null) {
                _movie.value = foundMovie
                val translatedOverview = TranslationService.translateToPtBr(foundMovie.overview)
                if (translatedOverview != foundMovie.overview) {
                    _movie.value = foundMovie.copy(overview = translatedOverview)
                }
            } else {
                _movie.value = null
            }
        }

        viewModelScope.launch {
            repository.isFavorite(movieId).collectLatest { isFav ->
                _isFavorite.value = isFav
            }
        }
    }

    fun toggleFavorite() {
        val currentMovie = _movie.value ?: return
        viewModelScope.launch {
            if (_isFavorite.value) {
                repository.removeFavorite(currentMovie)
            } else {
                repository.addFavorite(currentMovie)
            }
        }
    }
}
