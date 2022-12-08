package az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies

data class TopRatedMoviesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)