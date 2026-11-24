package com.example.myapplication.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("shows")
    suspend fun getPopularMovies(): List<MovieDto>

    @GET("search/shows")
    suspend fun searchMovies(@Query("q") query: String): List<TvMazeSearchDto>

    companion object {
        private const val BASE_URL = "https://api.tvmaze.com/"

        fun create(): MovieApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiService::class.java)
        }
    }
}
