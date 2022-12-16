package az.abb.tap.cinephilia.data.network.tmdb.model.moviecreditsresponse

data class MovieCreditsResponse(
    val cast: List<MovieCastResult>,
    val crew: List<Crew>,
    val id: Int
)