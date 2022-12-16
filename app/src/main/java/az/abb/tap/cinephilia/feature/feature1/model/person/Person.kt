package az.abb.tap.cinephilia.feature.feature1.model.person

data class Person(
    val name: String,
    val id: Int,
    val gender: Int,
    val knownForDepartment: String,
    val profilePath: String?,
    val popularity: Double
)