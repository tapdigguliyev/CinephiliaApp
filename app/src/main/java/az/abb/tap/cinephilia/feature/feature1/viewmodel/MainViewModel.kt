package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.abb.tap.cinephilia.data.network.tmdb.model.genres.GenresResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.ResultMovie
import az.abb.tap.cinephilia.data.network.tmdb.model.popularpeopleresponse.ResultPopularPeople
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.ResultSerie
import az.abb.tap.cinephilia.data.network.tmdb.model.seriesresponse.SeriesResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.trendingpeopleresponse.ResultTrendingPeople
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.handleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _movieGenres: MutableLiveData<Resource<GenresResponse>> = MutableLiveData()
    val movieGenres: LiveData<Resource<GenresResponse>> = _movieGenres

    private val _tvShowGenres: MutableLiveData<Resource<GenresResponse>> = MutableLiveData()
    val tvShowGenres: LiveData<Resource<GenresResponse>> = _tvShowGenres

    suspend fun getTrendingPeople(): LiveData<PagingData<ResultTrendingPeople>> {
        return mediaRepository.provideTrendingPeople().cachedIn(viewModelScope)
    }

    suspend fun getPopularPeople(): LiveData<PagingData<ResultPopularPeople>> {
        return mediaRepository.providePopularPeople().cachedIn(viewModelScope)
    }

    suspend fun getPopularMovieList(): LiveData<PagingData<ResultMovie>> {
        return mediaRepository.providePopularMovies().cachedIn(viewModelScope)
    }

    suspend fun getPopularTVShowsList(): LiveData<PagingData<ResultSerie>> {
        return mediaRepository.providePopularTVShows().cachedIn(viewModelScope)
    }

    fun getTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTopRatedMovies()
                _topRatedMovies.postValue(handleResponse(response))
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

    fun getTopRatedTVShows() = viewModelScope.launch {
        _topRatedTVShows.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTopRatedTVShows()
                _topRatedTVShows.postValue(handleResponse(response))
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

    fun getMovieGenres() = viewModelScope.launch {
        _movieGenres.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideMovieGenres()
                _movieGenres.postValue(handleResponse(response))
            } else {
                _movieGenres.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _movieGenres.postValue(Resource.Error("Network Failure"))
                else -> _movieGenres.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getTVShowGenres() = viewModelScope.launch {
        _tvShowGenres.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTVShowGenres()
                _tvShowGenres.postValue(handleResponse(response))
            } else {
                _tvShowGenres.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _tvShowGenres.postValue(Resource.Error("Network Failure"))
                else -> _tvShowGenres.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}