package az.abb.tap.cinephilia.data.network.tmdb.model.persontvshowcreditsresponse

data class PersonTVShowCreditsResponse(
    val cast: List<PersonTVShowCastResult>,
    val crew: List<Crew>,
    val id: Int
)