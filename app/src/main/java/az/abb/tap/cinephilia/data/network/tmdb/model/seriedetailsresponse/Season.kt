package az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse

data class Season(
    val air_date: String,
    val episode_count: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: Any,
    val season_number: Int
)