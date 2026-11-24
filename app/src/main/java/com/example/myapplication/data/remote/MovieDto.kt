package com.example.myapplication.data.remote

import com.google.gson.annotations.SerializedName

data class TvMazeSearchDto(
    @SerializedName("show") val show: MovieDto
)

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("premiered") val premiered: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("genres") val genres: List<String>?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("rating") val rating: RatingDto?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("image") val image: ImageDto?,
    @SerializedName("poster_path") val posterPath: String?
)

data class RatingDto(
    @SerializedName("average") val average: Double?
)

data class ImageDto(
    @SerializedName("medium") val medium: String?,
    @SerializedName("original") val original: String?
)
