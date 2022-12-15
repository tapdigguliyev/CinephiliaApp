package az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse

data class SeriesResponse(
    val page: Int,
    val results: List<ResultSerie>,
    val total_pages: Int,
    val total_results: Int
)