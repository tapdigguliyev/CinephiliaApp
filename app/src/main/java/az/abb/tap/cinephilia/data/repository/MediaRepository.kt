package az.abb.tap.cinephilia.data.repository

import androidx.paging.liveData
import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.utility.createPager
import retrofit2.Response

class MediaRepository(private val apiService: ApiService) : MediaProvider {

    override suspend fun provideTopRatedMovies(): Response<MoviesResponse> {
        return apiService.getTopRatedMovies()
    }

    override suspend fun providePopularMovies() = createPager { page ->
        apiService.getPopularMovies(page = page).body()?.results!!
    }

    override suspend fun provideMovieGenres(): Response<GenresResponse> {
        return apiService.getMovieGenres()
    }

    override suspend fun provideTopRatedTVShows(): Response<SeriesResponse> {
        return apiService.getTopRatedTvShows()
    }

    override suspend fun providePopularTVShows() = createPager { page ->
        apiService.getPopularTvShows(page = page).body()?.results!!
    }.liveData

    override suspend fun provideMovieDetails(movieId: Int): Response<MovieDetailsResponse> {
        return apiService.getMovieDetails(movieId)
    }

    override suspend fun provideSerieDetails(tvId: Int): Response<SerieDetailsResponse> {
        return apiService.getSerieDetails(tvId)
    }

    override suspend fun provideTVShowGenres(): Response<GenresResponse> {
        return apiService.getTVShowGenres()
    }
}