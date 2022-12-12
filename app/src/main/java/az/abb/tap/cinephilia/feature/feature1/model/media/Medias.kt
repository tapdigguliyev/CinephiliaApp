package az.abb.tap.cinephilia.feature.feature1.model.media

data class Medias (
    val page: Int,
    val movies: List<Media>,
    val total_pages: Int,
    val total_results: Int
)
