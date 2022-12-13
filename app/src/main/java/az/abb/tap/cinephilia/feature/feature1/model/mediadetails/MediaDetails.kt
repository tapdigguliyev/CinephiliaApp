package az.abb.tap.cinephilia.feature.feature1.model.mediadetails

import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre

data class MediaDetails(
    val id: Int,
    val genres: MutableList<Genre>,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val title: String,
    val vote_average: Double,
)