package az.abb.tap.cinephilia.data.network.tmdb

import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviecreditsresponse.MovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.popularpeopleresponse.PopularPeopleResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persondetailsresponse.PersonDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.personmoviecreditsresponse.PersonMovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persontvshowcreditsresponse.PersonTVShowCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse.TrendingPeopleResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.tvshowcreditsresponse.TVShowCreditsResponse
import az.abb.tap.cinephilia.utility.Ignore.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US",
        @Query("page")
        page: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US",
        @Query("page")
        page: Int = 1
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<MovieDetailsResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<GenresResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US",
        @Query("page")
        page: Int = 1
    ) : Response<SeriesResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US",
        @Query("page")
        page: Int = 1
    ) : Response<SeriesResponse>

    @GET("tv/{tv_id}")
    suspend fun getSerieDetails(
        @Path("tv_id")
        tvShowId: Int,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<SerieDetailsResponse>

    @GET("genre/tv/list")
    suspend fun getTVShowGenres(
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<GenresResponse>

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<PopularPeopleResponse>

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingPeople(
        @Path("media_type") mediaType: String = "person",
        @Path("time_window") timeWindow: String = "day",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<TrendingPeopleResponse>

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<PersonDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<PersonMovieCreditsResponse>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPersonTVShowCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<PersonTVShowCreditsResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<MovieCreditsResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTVShowCredits(
        @Path("tv_id") tvShowId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<TVShowCreditsResponse>
}