package az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse

data class ResultTrendingPeople(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for: List<KnownFor>,
    val known_for_department: String,
    val media_type: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)