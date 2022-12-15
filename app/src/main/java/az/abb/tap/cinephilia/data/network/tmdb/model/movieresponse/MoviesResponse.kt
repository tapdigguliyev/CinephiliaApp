package az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse

data class MoviesResponse(
    val page: Int,
    val results: List<ResultMovie>,
    val total_pages: Int,
    val total_results: Int
)