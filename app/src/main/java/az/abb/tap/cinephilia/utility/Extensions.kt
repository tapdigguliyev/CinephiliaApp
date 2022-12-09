package az.abb.tap.cinephilia.utility

import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenreInfo
import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.Result
import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.TopRatedMoviesResponse
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.movies.Movie
import az.abb.tap.cinephilia.feature.feature1.model.movies.MoviesResponse

fun TopRatedMoviesResponse.toMoviesResponse() =
    MoviesResponse(
        page = page,
        movies = results.map { it.toMovie() },
        total_pages = total_pages,
        total_results = total_results
    )

fun Result.toMovie() =
    Movie(
        title = title,
        originalTitle = original_title,
        genreIds = genre_ids,
        overview = overview,
        imageLink = String.format("https://image.tmdb.org/t/p/original%s", poster_path),
        releaseDate = release_date
    )

fun GenreInfo.toGenre() =
    Genre(
        id = id,
        name = name
    )