package az.abb.tap.cinephilia.data.repository

import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import retrofit2.Response

interface MediaProvider {
    suspend fun provideTopRatedMovies(): Response<MoviesResponse>
    suspend fun providePopularMovies(): Response<MoviesResponse>
    suspend fun provideMovieGenres(): Response<GenresResponse>
    suspend fun provideTopRatedTVShows(): Response<SeriesResponse>
    suspend fun providePopularTVShows(): Response<SeriesResponse>
    suspend fun provideMovieDetails(movieId: Int): Response<MovieDetailsResponse>
}