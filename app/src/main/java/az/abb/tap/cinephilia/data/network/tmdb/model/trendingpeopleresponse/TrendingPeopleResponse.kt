package az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse

data class TrendingPeopleResponse(
    val page: Int,
    val results: List<ResultTrendingPeople>,
    val total_pages: Int,
    val total_results: Int
)