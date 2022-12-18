package az.abb.tap.cinephilia.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviecreditsresponse.MovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.ResultMovie
import az.abb.tap.cinephilia.data.network.tmdb.model.popularpeopleresponse.ResultPopularPeople
import az.abb.tap.cinephilia.data.network.tmdb.model.persondetailsresponse.PersonDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.personmoviecreditsresponse.PersonMovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persontvshowcreditsresponse.PersonTVShowCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSerie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse.ResultTrendingPeople
import az.abb.tap.cinephilia.data.network.tmdb.model.tvshowcreditsresponse.TVShowCreditsResponse
import retrofit2.Response

interface MediaProvider {
    suspend fun provideTopRatedMovies(): LiveData<PagingData<ResultMovie>>
    suspend fun providePopularMovies(): LiveData<PagingData<ResultMovie>>
    suspend fun provideMovieGenres(): Response<GenresResponse>
    suspend fun provideTopRatedTVShows(): LiveData<PagingData<ResultSerie>>
    suspend fun providePopularTVShows(): LiveData<PagingData<ResultSerie>>
    suspend fun provideMovieDetails(movieId: Int): Response<MovieDetailsResponse>
    suspend fun provideSerieDetails(tvId: Int): Response<SerieDetailsResponse>
    suspend fun provideTVShowGenres(): Response<GenresResponse>
    suspend fun providePopularPeople(): LiveData<PagingData<ResultPopularPeople>>
    suspend fun provideTrendingPeople(): LiveData<PagingData<ResultTrendingPeople>>
    suspend fun providePersonDetails(personId: Int): Response<PersonDetailsResponse>
    suspend fun provideMovieCredits(movieId: Int): Response<MovieCreditsResponse>
    suspend fun provideTVShowCredits(tvShowId: Int): Response<TVShowCreditsResponse>
    suspend fun providePersonMovieCredits(personId: Int): Response<PersonMovieCreditsResponse>
    suspend fun providePersonTVShowCredits(personId: Int): Response<PersonTVShowCreditsResponse>
}