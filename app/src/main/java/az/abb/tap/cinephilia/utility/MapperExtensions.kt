package az.abb.tap.cinephilia.utility

import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenreInfo
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.DetailGenre
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.ResultMovie
import az.abb.tap.cinephilia.data.network.tmdb.model.popularpeopleresponse.ResultPopularPeople
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SeriesGenre
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSerie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse.ResultTrendingPeople
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.media.Media
import az.abb.tap.cinephilia.feature.feature1.model.media.Medias
import az.abb.tap.cinephilia.feature.feature1.model.mediadetails.MediaDetails
import az.abb.tap.cinephilia.feature.feature1.model.person.Person
import az.abb.tap.cinephilia.utility.Constants.IMAGE_BASE_URL

fun MoviesResponse.toMedias() =
    Medias(
        page = page,
        movies = results.map { it.toMedia() },
        total_pages = total_pages,
        total_results = total_results
    )

fun ResultMovie.toMedia() =
    Media(
        id = id,
        title = title,
        originalTitle = original_title,
        genreIds = genre_ids,
        overview = overview,
        imageLink = String.format(IMAGE_BASE_URL, poster_path),
        releaseDate = release_date,
        language = original_language,
        rating = vote_average
    )

fun SeriesResponse.toMedias() =
    Medias(
        page = page,
        movies = results.map { it.toMedia() },
        total_pages = total_pages,
        total_results = total_results
    )

fun ResultSerie.toMedia() =
    Media(
        id = id,
        title = name,
        originalTitle = original_name,
        genreIds = genre_ids,
        overview = overview,
        imageLink = String.format(IMAGE_BASE_URL, poster_path),
        releaseDate = first_air_date,
        language = original_language,
        rating = vote_average
    )

fun GenreInfo.toGenre() =
    Genre(
        id = id,
        name = name
    )

fun MovieDetailsResponse.toMediaDetails() =
    MediaDetails(
        id = id,
        genres = genres.map { it.toNewGenre() }.toMutableList(),
        original_title = original_title,
        overview = overview,
        poster_path = String.format(IMAGE_BASE_URL, poster_path),
        release_date = release_date,
        runtime = runtime,
        title = title,
        vote_average = vote_average
    )

fun SerieDetailsResponse.toMediaDetails() =
    MediaDetails(
        id = id,
        genres = genres.map { it.toNewGenre() }.toMutableList(),
        original_title = original_name,
        overview = overview,
        poster_path = String.format(IMAGE_BASE_URL, poster_path),
        release_date = first_air_date,
        runtime = 0,
        title = name,
        vote_average = vote_average
    )

fun DetailGenre.toNewGenre() =
    Genre(
        id = id,
        name = name
    )

fun SeriesGenre.toNewGenre() =
    Genre(
        id = id,
        name = name
    )

fun ResultTrendingPeople.toPerson() =
    Person(
        name = name,
        id = id,
        gender = gender,
        knownForDepartment = known_for_department,
        profilePath = if (profile_path == null) null else String.format(IMAGE_BASE_URL, profile_path),
        popularity = popularity
    )

fun ResultPopularPeople.toPerson() =
    Person(
        name = name,
        id = id,
        gender = gender,
        knownForDepartment = known_for_department,
        profilePath = if (profile_path == null) null else String.format(IMAGE_BASE_URL, profile_path),
        popularity = popularity
    )