package az.abb.tap.cinephilia.data.repository

import androidx.paging.liveData
import az.abb.tap.cinephilia.data.network.tmdb.ApiService
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviecreditsresponse.MovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persondetailsresponse.PersonDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.personmoviecreditsresponse.PersonMovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persontvshowcreditsresponse.PersonTVShowCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.tvshowcreditsresponse.TVShowCreditsResponse
import az.abb.tap.cinephilia.utility.createPager
import retrofit2.Response

class MediaRepository(private val apiService: ApiService) : MediaProvider {

    override suspend fun provideTopRatedMovies() = createPager { page ->
        apiService.getTopRatedMovies(page = page).body()?.results!!
    }.liveData

    override suspend fun providePopularMovies() = createPager { page ->
        apiService.getPopularMovies(page = page).body()?.results!!
    }.liveData

    override suspend fun provideMovieGenres(): Response<GenresResponse> {
        return apiService.getMovieGenres()
    }

    override suspend fun provideTopRatedTVShows() = createPager { page ->
        apiService.getTopRatedTvShows(page = page).body()?.results!!
    }.liveData

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

    override suspend fun providePopularPeople() = createPager { page ->
        apiService.getPopularPeople(page = page).body()?.results!!
    }.liveData

    override suspend fun provideTrendingPeople() = createPager { page ->
        apiService.getTrendingPeople(page = page).body()?.results!!
    }.liveData

    override suspend fun providePersonDetails(personId: Int): Response<PersonDetailsResponse> {
        return apiService.getPersonDetails(personId)
    }

    override suspend fun provideMovieCredits(movieId: Int): Response<MovieCreditsResponse> {
        return apiService.getMovieCredits(movieId)
    }

    override suspend fun provideTVShowCredits(tvShowId: Int): Response<TVShowCreditsResponse> {
        return apiService.getTVShowCredits(tvShowId)
    }

    override suspend fun providePersonMovieCredits(personId: Int): Response<PersonMovieCreditsResponse> {
        return apiService.getPersonMovieCredits(personId)
    }

    override suspend fun providePersonTVShowCredits(personId: Int): Response<PersonTVShowCreditsResponse> {
        return apiService.getPersonTVShowCredits(personId)
    }
}