package az.abb.tap.cinephilia.utility

import android.view.View
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenreInfo
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.Result
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.movies.Movie
import az.abb.tap.cinephilia.feature.feature1.model.movies.Movies

fun MoviesResponse.toMovies() =
    Movies(
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

fun List<String>.toStr(): String {
    val builder = StringBuilder()
    this.forEach { string ->
        builder.append(string)
        if (this.last() != string) builder.append(", ")
    }
    return builder.toString()
}

fun Movie.getListOfSpecificGenreNames(movieGenres: MutableList<Genre>): List<String> {
    val specificMovieGenres: MutableList<Genre> = mutableListOf()
    this.genreIds.forEach { genreId ->
        specificMovieGenres.addAll(movieGenres.filter { it.id == genreId })
    }
    return specificMovieGenres.map { it.name }
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}