package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val releaseYear: String,
    val genre: String,
    val overview: String,
    val rating: Double,
    val posterUrl: String = ""
)
