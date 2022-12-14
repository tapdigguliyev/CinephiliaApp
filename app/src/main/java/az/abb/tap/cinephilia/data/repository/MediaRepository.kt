package az.abb.tap.cinephilia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.Result
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.utility.Constants.NETWORK_PAGE_SIZE
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

    override suspend fun provideTVShowGenres(): Response<GenresResponse> {
        return apiService.getTVShowGenres()
    }

    fun getAllMovies(): LiveData<PagingData<Result>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                MoviePagingSource(apiService)
            }
            , initialKey = 1
        ).liveData
    }
}