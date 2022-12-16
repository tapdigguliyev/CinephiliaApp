package az.abb.tap.cinephilia.data.network.tmdb.model.personmoviecreditsresponse

data class PersonMovieCreditsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)