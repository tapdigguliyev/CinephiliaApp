package az.abb.tap.cinephilia.data.repository

import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.TopRatedMoviesResponse
import retrofit2.Response

interface MediaProvider {
    suspend fun provideMedia(): Response<TopRatedMoviesResponse>
}