package az.abb.tap.cinephilia.data.repository

import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.TopRatedMoviesResponse
import retrofit2.Response

class MediaRepository(private val apiService: ApiService) : MediaProvider {

    override suspend fun provideTopRatedMovies(): Response<TopRatedMoviesResponse> {
        return apiService.getTopRatedMovies()
    }

    override suspend fun provideMovieGenres(): Response<GenresResponse> {
        return apiService.getMovieGenres()
    }
}