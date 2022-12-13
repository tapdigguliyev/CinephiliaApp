package az.abb.tap.cinephilia.data.repository

import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
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

    override suspend fun provideTopRatedTVShows(): Response<SeriesResponse> {
        return apiService.getTopRatedTvShows()
    }

    override suspend fun providePopularTVShows(): Response<SeriesResponse> {
        return apiService.getPopularTvShows()
    }

    override suspend fun provideMovieDetails(movieId: Int): Response<MovieDetailsResponse> {
        return apiService.getMovieDetails(movieId)
    }

    override suspend fun provideSerieDetails(tvId: Int): Response<SerieDetailsResponse> {
        return apiService.getSerieDetails(tvId)
    }
}