package com.example.myapplication.data.repository

import com.example.myapplication.data.local.MovieDao
import com.example.myapplication.data.remote.MovieApiService
import com.example.myapplication.domain.model.Movie
import com.example.myapplication.domain.model.toDomain
import com.example.myapplication.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val apiService: MovieApiService = MovieApiService.create(),
    private val movieDao: MovieDao
) {
    companion object {
        @Volatile
        private var cachedMovies: List<Movie> = emptyList()
    }

    suspend fun getPopularMovies(): Result<List<Movie>> {
        return try {
            val dtos = apiService.getPopularMovies()
            val movies = dtos.map { it.toDomain() }
            if (movies.isNotEmpty()) {
                cachedMovies = movies
                Result.success(movies)
            } else {
                val fallback = getFallbackMovies()
                cachedMovies = fallback
                Result.success(fallback)
            }
        } catch (e: Exception) {
            val fallback = getFallbackMovies()
            cachedMovies = fallback
            Result.success(fallback)
        }
    }

    suspend fun searchMovies(query: String): Result<List<Movie>> {
        if (query.isBlank()) return getPopularMovies()
        return try {
            val searchResults = apiService.searchMovies(query)
            val movies = searchResults.map { it.show.toDomain() }
            if (movies.isNotEmpty()) {
                val newCache = (cachedMovies + movies).distinctBy { it.id }
                cachedMovies = newCache
                Result.success(movies)
            } else {
                val filtered = cachedMovies.filter {
                    it.title.contains(query, ignoreCase = true) || it.genre.contains(query, ignoreCase = true)
                }
                Result.success(filtered)
            }
        } catch (e: Exception) {
            val filtered = cachedMovies.filter {
                it.title.contains(query, ignoreCase = true) || it.genre.contains(query, ignoreCase = true)
            }
            Result.success(filtered)
        }
    }

    fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)
    }

    suspend fun addFavorite(movie: Movie) {
        movieDao.insertFavorite(movie.toEntity())
    }

    suspend fun removeFavorite(movie: Movie) {
        movieDao.deleteFavorite(movie.toEntity())
    }

    suspend fun getMovieById(movieId: Int): Movie? {
        cachedMovies.find { it.id == movieId }?.let { return it }

        val popularResult = getPopularMovies()
        popularResult.getOrNull()?.find { it.id == movieId }?.let { return it }

        return movieDao.getFavoriteById(movieId)?.toDomain()
    }

    private fun getFallbackMovies(): List<Movie> {
        return listOf(
            Movie(
                id = 1,
                title = "O Poderoso Chefão",
                releaseYear = "1972",
                genre = "Crime, Drama",
                overview = "O patriarca idoso de uma dinastia do crime organizado transfere o controle de seu império clandestino para seu filho relutante.",
                rating = 9.2,
                posterUrl = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg"
            ),
            Movie(
                id = 2,
                title = "Batman: O Cavaleiro das Trevas",
                releaseYear = "2008",
                genre = "Ação, Crime, Drama",
                overview = "Quando a ameaça conhecida como Coringa emerge de seu passado misterioso, ele causa estragos e caos no povo de Gotham.",
                rating = 9.0,
                posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"
            ),
            Movie(
                id = 3,
                title = "A Origem",
                releaseYear = "2010",
                genre = "Ação, Ficção Científica, Aventura",
                overview = "Um ladrão que rouba segredos corporativos por meio do uso da tecnologia de compartilhamento de sonhos recebe a tarefa inversa de plantar uma idéia na mente de um C.E.O.",
                rating = 8.8,
                posterUrl = "https://image.tmdb.org/t/p/w500/ljs28On2sYmTHuv224wfZz4L92D.jpg"
            ),
            Movie(
                id = 4,
                title = "Interstellar",
                releaseYear = "2014",
                genre = "Ficção Científica, Drama, Aventura",
                overview = "Uma equipe de exploradores viaja através de um buraco de minhoca no espaço na tentativa de garantir a sobrevivência da humanidade.",
                rating = 8.6,
                posterUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlJJvJe.jpg"
            ),
            Movie(
                id = 5,
                title = "Matrix",
                releaseYear = "1999",
                genre = "Ação, Ficção Científica",
                overview = "Um programador de computador descobre um mundo secreto mantido por inteligência artificial que escravizou a humanidade em uma realidade simulada.",
                rating = 8.7,
                posterUrl = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
            ),
            Movie(
                id = 6,
                title = "Vingadores: Ultimato",
                releaseYear = "2019",
                genre = "Ação, Aventura, Ficção Científica",
                overview = "Após os eventos devastadores de Vingadores: Guerra Infinita, o universo está em ruínas. Com a ajuda dos aliados restantes, os Vingadores se reúnem mais uma vez.",
                rating = 8.4,
                posterUrl = "https://image.tmdb.org/t/p/w500/ulHFrmwB75TStOiWsOIa3E81Rio.jpg"
            )
        )
    }
}
