package az.abb.tap.cinephilia.data.network.tmdb.model.popularpeopleresponse

data class PopularPeopleResponse(
    val page: Int,
    val results: List<ResultPopularPeople>,
    val total_pages: Int,
    val total_results: Int
)