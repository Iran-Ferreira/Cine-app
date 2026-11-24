package com.example.myapplication.domain.model

import com.example.myapplication.data.local.MovieEntity
import com.example.myapplication.data.remote.MovieDto

data class Movie(
    val id: Int,
    val title: String,
    val releaseYear: String,
    val genre: String,
    val overview: String,
    val rating: Double,
    val posterUrl: String
)

fun MovieDto.toDomain(): Movie {
    val movieTitle = name ?: title ?: "Sem título"
    val releaseStr = premiered ?: releaseDate ?: ""
    val year = releaseStr.takeIf { it.length >= 4 }?.substring(0, 4) ?: "N/A"

    val genreName = when {
        !genres.isNullOrEmpty() -> genres.joinToString(", ")
        else -> mapGenreIdsToNames(genreIds)
    }

    val rawSummary = summary ?: overview ?: "Sem sinopse disponível."
    val cleanOverview = rawSummary.replace(Regex("<[^>]*>"), "").ifBlank { "Sem sinopse disponível." }

    val rate = rating?.average ?: voteAverage ?: 0.0

    val poster = when {
        image?.original != null -> image.original
        image?.medium != null -> image.medium
        !posterPath.isNullOrEmpty() -> "https://image.tmdb.org/t/p/w500$posterPath"
        else -> ""
    }

    return Movie(
        id = id,
        title = movieTitle,
        releaseYear = year,
        genre = genreName,
        overview = cleanOverview,
        rating = rate,
        posterUrl = poster
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        releaseYear = releaseYear,
        genre = genre,
        overview = overview,
        rating = rating,
        posterUrl = posterUrl
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        releaseYear = releaseYear,
        genre = genre,
        overview = overview,
        rating = rating,
        posterUrl = posterUrl
    )
}

private fun mapGenreIdsToNames(genreIds: List<Int>?): String {
    if (genreIds.isNullOrEmpty()) return "Geral"
    val genresMap = mapOf(
        28 to "Ação",
        12 to "Aventura",
        16 to "Animação",
        35 to "Comédia",
        80 to "Crime",
        99 to "Documentário",
        18 to "Drama",
        10751 to "Família",
        14 to "Fantasia",
        36 to "História",
        27 to "Terror",
        10402 to "Música",
        9648 to "Mistério",
        10749 to "Romance",
        878 to "Ficção Científica",
        10770 to "Cinema TV",
        53 to "Suspense",
        10752 to "Guerra",
        37 to "Faroeste"
    )
    return genreIds.mapNotNull { genresMap[it] }.joinToString(", ").ifEmpty { "Geral" }
}
