package az.abb.tap.cinephilia.feature.feature1.model.movies

data class MoviesResponse (
    val page: Int,
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)
