package az.abb.tap.cinephilia.utility

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenreInfo
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.DetailGenre
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.Result
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSeries
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.media.Media
import az.abb.tap.cinephilia.feature.feature1.model.media.Medias
import az.abb.tap.cinephilia.feature.feature1.model.moviedetails.MovieDetails
import com.bumptech.glide.RequestManager

fun MoviesResponse.toMedias() =
    Medias(
        page = page,
        movies = results.map { it.toMedia() },
        total_pages = total_pages,
        total_results = total_results
    )

fun Result.toMedia() =
    Media(
        id = id,
        title = title,
        originalTitle = original_title,
        genreIds = genre_ids,
        overview = overview,
        imageLink = String.format("https://image.tmdb.org/t/p/original%s", poster_path),
        releaseDate = release_date
    )

fun SeriesResponse.toMedias() =
    Medias(
        page = page,
        movies = results.map { it.toMedia() },
        total_pages = total_pages,
        total_results = total_results
    )

fun ResultSeries.toMedia() =
    Media(
        id = id,
        title = name,
        originalTitle = original_name,
        genreIds = genre_ids,
        overview = overview,
        imageLink = String.format("https://image.tmdb.org/t/p/original%s", poster_path),
        releaseDate = first_air_date
    )

fun GenreInfo.toGenre() =
    Genre(
        id = id,
        name = name
    )

fun MovieDetailsResponse.toMovieDetails() =
    MovieDetails(
        id = id,
        genres = genres.map { it.toNewGenre() }.toMutableList(),
        original_title = original_title,
        overview = overview,
        poster_path = String.format("https://image.tmdb.org/t/p/original%s", poster_path),
        release_date = release_date,
        runtime = runtime,
        title = title,
        vote_average = vote_average
    )

fun DetailGenre.toNewGenre() =
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

fun Media.getListOfSpecificGenreNames(movieGenres: MutableList<Genre>): List<String> {
    val specificMovieGenres: MutableList<Genre> = mutableListOf()
    this.genreIds.forEach { genreId ->
        specificMovieGenres.addAll(movieGenres.filter { it.id == genreId })
    }
    return specificMovieGenres.map { it.name }
}

fun MovieDetails.getGenreNames(): List<String> {
    val genreNames: MutableList<String> = mutableListOf()
    this.genres.forEach { genre ->
        genreNames.add(genre.name)
    }
    return genreNames
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun RecyclerView.snapToChildView() {
    LinearSnapHelper().attachToRecyclerView(this)
}

fun View.assignColors(context: Context, imageLink: String, glide: RequestManager, vararg textView: TextView) {
    val bitmap = glide.asBitmap().load(imageLink).submit().get()

    Palette.from(bitmap).generate {
        it?.let {
            val backgroundColor = it.getDominantColor(
                ContextCompat.getColor(
                    context,
                    androidx.appcompat.R.color.background_material_dark
                )
            )

            when(this) {
                is CardView -> setCardBackgroundColor(backgroundColor)
                else -> setBackgroundColor(backgroundColor)
            }

            textView.forEach { textView ->
                textView.setTextColorAgainstBackgroundColor(backgroundColor)
            }
        }
    }
}

fun TextView.setTextColorAgainstBackgroundColor(backgroundColor: Int) {
    val textColor = if (backgroundColor.isDark()) Color.WHITE else Color.BLACK
    this.setTextColor(textColor)
}

fun Int.isDark(): Boolean {
    val darkness = 1 - (0.299 * Color.red(this) +
            0.587 * Color.green(this) +
            0.114 * Color.blue(this)) / 255
    return darkness >= 0.5
}

fun String.getYearFromDate() =
    this.split("-")[0]