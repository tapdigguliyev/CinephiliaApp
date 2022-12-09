package az.abb.tap.cinephilia.feature.feature1.model.movies

data class Movie (
    val title: String,
    val originalTitle: String,
    val genreIds: List<Int>,
    val overview: String,
    val imageLink: String,
    val releaseDate: String
)