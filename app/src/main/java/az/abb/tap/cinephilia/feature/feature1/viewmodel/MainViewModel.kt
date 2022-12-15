package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.ResultMovie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSerie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.toGenre
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

    private val _topRatedTVShows: MutableLiveData<Resource<SeriesResponse>> = MutableLiveData()
    val topRatedTVShows: LiveData<Resource<SeriesResponse>> = _topRatedTVShows

    var movieGenres: MutableList<Genre> = mutableListOf()
    var tVShowGenres: MutableList<Genre> = mutableListOf()

    init {
        getTopRatedMovies()
        getMovieGenres()
        getTVShowGenres()
        getTopRatedTVShows()
    }

    suspend fun getPopularMovieList(): LiveData<PagingData<ResultMovie>> {
        return mediaRepository.providePopularMovies().liveData.cachedIn(viewModelScope)
    }

    suspend fun getPopularTVShowsList(): LiveData<PagingData<ResultSerie>> {
        return mediaRepository.providePopularTVShows().cachedIn(viewModelScope)
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

    private fun getMovieGenres() = viewModelScope.launch {
        if (networkStatusChecker.hasInternetConnection()) {
            val response = mediaRepository.provideMovieGenres()
            if (response.isSuccessful) {
                movieGenres.addAll(response.body()?.genres?.map { it.toGenre() }!!)
            }
        }
    }

    private fun getTVShowGenres() = viewModelScope.launch {
        if (networkStatusChecker.hasInternetConnection()) {
            val response = mediaRepository.provideTVShowGenres()
            if (response.isSuccessful) {
                tVShowGenres.addAll(response.body()?.genres?.map { it.toGenre() }!!)
            }
        }
    }
}