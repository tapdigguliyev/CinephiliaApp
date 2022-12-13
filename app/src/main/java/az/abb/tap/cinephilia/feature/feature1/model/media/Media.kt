package az.abb.tap.cinephilia.feature.feature1.model.media

data class Media (
    val id: Int,
    val title: String,
    val originalTitle: String,
    val genreIds: List<Int>,
    val overview: String,
    val imageLink: String,
    val releaseDate: String
)