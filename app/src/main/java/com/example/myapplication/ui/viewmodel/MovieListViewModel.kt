package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface MovieListUiState {
    object Loading : MovieListUiState
    data class Success(val movies: List<Movie>) : MovieListUiState
    data class Error(val message: String) : MovieListUiState
}

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    val repository: MovieRepository = MovieRepository(
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    init {
        fetchMovies()
        observeFavorites()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = MovieListUiState.Loading
            val result = repository.getPopularMovies()
            result.onSuccess { movies ->
                _uiState.value = MovieListUiState.Success(movies)
            }.onFailure { error ->
                _uiState.value = MovieListUiState.Error(error.message ?: "Erro ao carregar filmes.")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.isBlank()) {
                fetchMovies()
            } else {
                _uiState.value = MovieListUiState.Loading
                val result = repository.searchMovies(query)
                result.onSuccess { movies ->
                    if (movies.isNotEmpty()) {
                        _uiState.value = MovieListUiState.Success(movies)
                    } else {
                        _uiState.value = MovieListUiState.Error("Nenhum filme ou série encontrado para '$query'.")
                    }
                }.onFailure {
                    _uiState.value = MovieListUiState.Error("Erro ao pesquisar filmes.")
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoriteMovies().collectLatest { favorites ->
                _favoriteIds.value = favorites.map { it.id }.toSet()
            }
        }
    }
}
