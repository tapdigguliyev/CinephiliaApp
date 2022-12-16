package az.abb.tap.cinephilia.data.network.tmdb.model.tvshowcreditsresponse

data class TVShowCreditsResponse(
    val cast: List<TVShowCastResult>,
    val crew: List<Crew>,
    val id: Int
)