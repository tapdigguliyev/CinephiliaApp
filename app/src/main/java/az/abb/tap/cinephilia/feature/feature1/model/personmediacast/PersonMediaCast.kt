package az.abb.tap.cinephilia.feature.feature1.model.personmediacast

data class PersonMediaCast(
    val character: String,
    val id: Int,
    val posterPath: String?,
    val releaseDate: String,
    val mediaName: String,
    val voteAverage: Double
)