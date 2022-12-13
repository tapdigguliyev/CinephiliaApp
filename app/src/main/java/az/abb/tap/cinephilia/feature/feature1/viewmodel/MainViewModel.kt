package az.abb.tap.cinephilia.feature.feature1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.feature.feature1.model.moviedetails.MovieDetails
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.toGenre
import az.abb.tap.cinephilia.utility.toMovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val networkStatusChecker: NetworkStatusChecker
    ) : ViewModel() {

    private val _topRatedMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val topRatedMovies: LiveData<Resource<MoviesResponse>> = _topRatedMovies

    private val _popularMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val popularMovies: LiveData<Resource<MoviesResponse>> = _popularMovies

    private val _topRatedTVShows: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val topRatedTVShows: LiveData<Resource<SeriesResponse>> = _topRatedTVShows

    private val _popularTVShows: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val popularTVShows: LiveData<Resource<SeriesResponse>> = _popularTVShows

    var movieGenres: MutableList<Genre> = mutableListOf()

    var movie: MovieDetails? = null

    init {
        getTopRatedMovies()
        getPopularMovies()
        getGenres()
        getTopRatedTVShows()
        getPopularTVShows()
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTopRatedMovies()
                _topRatedMovies.postValue(handleMoviesResponse(response))
            } else {
                _topRatedMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _topRatedMovies.postValue(Resource.Error("Network Failure"))
                else -> _topRatedMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _popularMovies.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.providePopularMovies()
                _popularMovies.postValue(handleMoviesResponse(response))
            } else {
                _popularMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _popularMovies.postValue(Resource.Error("Network Failure"))
                else -> _popularMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        if (networkStatusChecker.hasInternetConnection()) {
            val response = mediaRepository.provideMovieDetails(movieId)
            if (response.isSuccessful) {
                movie = response.body()?.toMovieDetails()
                Log.d("MainViewModel", movie?.title ?: "Null")
            }
        }
    }

    private fun getTopRatedTVShows() = viewModelScope.launch {
        _topRatedTVShows.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTopRatedTVShows()
                _topRatedTVShows.postValue(handleSeriesResponse(response))
            } else {
                _topRatedTVShows.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _topRatedTVShows.postValue(Resource.Error("Network Failure"))
                else -> _topRatedTVShows.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun getPopularTVShows() = viewModelScope.launch {
        _popularTVShows.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.providePopularTVShows()
                _popularTVShows.postValue(handleSeriesResponse(response))
            } else {
                _popularTVShows.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _popularTVShows.postValue(Resource.Error("Network Failure"))
                else -> _popularTVShows.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSeriesResponse(response: Response<SeriesResponse>): Resource<SeriesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun getGenres() = viewModelScope.launch {
        if (networkStatusChecker.hasInternetConnection()) {
            val response = mediaRepository.provideMovieGenres()
            if (response.isSuccessful) {
                movieGenres.addAll(response.body()?.genres?.map { it.toGenre() }!!)
            }
        }
    }
}