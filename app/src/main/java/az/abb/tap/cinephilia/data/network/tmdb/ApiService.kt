package az.abb.tap.cinephilia.data.network.tmdb

import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.TopRatedMoviesResponse
import az.abb.tap.cinephilia.utility.Ignore.API_KEY
import retrofit2.Response
import retrofit2.http.GET
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
    ): Response<TopRatedMoviesResponse>
}