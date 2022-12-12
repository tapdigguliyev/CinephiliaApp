package az.abb.tap.cinephilia.data.repository

import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import retrofit2.Response

class MediaRepository(private val apiService: ApiService) : MediaProvider {

    override suspend fun provideTopRatedMovies(): Response<MoviesResponse> {
        return apiService.getTopRatedMovies()
    }

    override suspend fun providePopularMovies(): Response<MoviesResponse> {
        return apiService.getPopularMovies()
    }

    override suspend fun provideMovieGenres(): Response<GenresResponse> {
        return apiService.getMovieGenres()
    }

    override suspend fun provideTopRatedTVShows(): Response<MoviesResponse> {
        return apiService.getTopRatedTvShows()
    }
}