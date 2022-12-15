package az.abb.tap.cinephilia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.ResultMovie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSerie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import retrofit2.Response

interface MediaProvider {
    suspend fun provideTopRatedMovies(): Response<MoviesResponse>
    suspend fun providePopularMovies(): Pager<Int, ResultMovie>
    suspend fun provideMovieGenres(): Response<GenresResponse>
    suspend fun provideTopRatedTVShows(): Response<SeriesResponse>
    suspend fun providePopularTVShows(): LiveData<PagingData<ResultSerie>>
    suspend fun provideMovieDetails(movieId: Int): Response<MovieDetailsResponse>
    suspend fun provideSerieDetails(tvId: Int): Response<SerieDetailsResponse>
    suspend fun provideTVShowGenres(): Response<GenresResponse>
}