package az.abb.tap.cinephilia.feature.feature1.model.persondetails

data class PersonDetails(
    val biography: String,
    val birthday: String,
    val deathDay: String?,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val placeOfBirth: String,
    val popularity: Double,
    val profilePath: String?
)