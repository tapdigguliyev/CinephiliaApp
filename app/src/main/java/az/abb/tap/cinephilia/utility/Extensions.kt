package az.abb.tap.cinephilia.utility

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.substring
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.media.Media
import az.abb.tap.cinephilia.feature.feature1.model.mediacast.MediaCast
import az.abb.tap.cinephilia.feature.feature1.model.mediadetails.MediaDetails
import az.abb.tap.cinephilia.feature.feature1.model.person.Person
import az.abb.tap.cinephilia.feature.feature1.model.personmediacast.PersonMediaCast
import com.bumptech.glide.RequestManager

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

fun MediaDetails.getGenreNames(): List<String> {
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

fun View.makeGone() {
    this.visibility = View.GONE
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

fun Media.idBundle(mediaType: String) =
    Bundle().apply {
        putInt("mediaId", id)
        putString("mediaType", mediaType)
    }

fun Person.idBundle() =
    Bundle().apply {
        putInt("personId", id)
    }

fun MediaCast.idBundle() =
    Bundle().apply {
        putInt("personId", id)
    }

fun PersonMediaCast.idBundle(mediaType: String) =
    Bundle().apply {
        putInt("mediaId", id)
        putString("mediaType", mediaType)
    }

fun Double.outOfTen() =
    if (this == 10.0) substring(this.toString(), 0, 4) + "/10"
    else substring(this.toString(), 0, 3) + "/10"

fun CombinedLoadStates.setup(context: Context, progressBar: ProgressBar) {
    if (this.refresh is LoadState.Loading ||
        this.append is LoadState.Loading)
        progressBar.makeVisible()
    else {
        progressBar.makeInvisible()
        val errorState = when {
            this.append is LoadState.Error -> this.append as LoadState.Error
            this.prepend is LoadState.Error ->  this.prepend as LoadState.Error
            this.refresh is LoadState.Error -> this.refresh as LoadState.Error
            else -> null
        }
        errorState?.let {
            Toast.makeText(context, it.error.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

fun String.setAsKnownFor() =
    String.format("Known for: %s", this)

fun Int.setAsGenderString() =
    when(this){
        1 -> "Female"
        2 -> "Male"
        else -> ""
    }

fun Double.getBeautifulString() =
    String.format("Popularity: %s", this.toString().substringBefore("."))