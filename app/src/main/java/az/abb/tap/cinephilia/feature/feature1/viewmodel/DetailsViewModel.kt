package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.abb.tap.cinephilia.data.network.tmdb.model.moviecreditsresponse.MovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persondetailsresponse.PersonDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.personmoviecreditsresponse.PersonMovieCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.persontvshowcreditsresponse.PersonTVShowCreditsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.seriedetailsresponse.SerieDetailsResponse
import az.abb.tap.cinephilia.data.network.tmdb.model.tvshowcreditsresponse.TVShowCreditsResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.handleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val networkStatusChecker: NetworkStatusChecker
) : ViewModel() {

    private val _movie: MutableLiveData<Resource<MovieDetailsResponse>> = MutableLiveData()
    val movie: LiveData<Resource<MovieDetailsResponse>> = _movie

    private val _serie: MutableLiveData<Resource<SerieDetailsResponse>> = MutableLiveData()
    val serie: LiveData<Resource<SerieDetailsResponse>> = _serie

    private val _movieCredits: MutableLiveData<Resource<MovieCreditsResponse>> = MutableLiveData()
    val movieCredits: LiveData<Resource<MovieCreditsResponse>> = _movieCredits

    private val _tvShowCredits: MutableLiveData<Resource<TVShowCreditsResponse>> = MutableLiveData()
    val tvShowCredits: LiveData<Resource<TVShowCreditsResponse>> = _tvShowCredits

    private val _person: MutableLiveData<Resource<PersonDetailsResponse>> = MutableLiveData()
    val person: LiveData<Resource<PersonDetailsResponse>> = _person

    private val _personMovieCredits: MutableLiveData<Resource<PersonMovieCreditsResponse>> = MutableLiveData()
    val personMovieCredits: LiveData<Resource<PersonMovieCreditsResponse>> = _personMovieCredits

    private val _personTVShowCredits: MutableLiveData<Resource<PersonTVShowCreditsResponse>> = MutableLiveData()
    val personTVShowCredits: LiveData<Resource<PersonTVShowCreditsResponse>> = _personTVShowCredits

    fun getPersonMovieCredits(personId: Int) = viewModelScope.launch {
        _personMovieCredits.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.providePersonMovieCredits(personId)
                _personMovieCredits.postValue(handleResponse(response))
            } else {
                _personMovieCredits.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _personMovieCredits.postValue(Resource.Error("Network Failure"))
                else -> _personMovieCredits.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getPersonTVShowCredits(personId: Int) = viewModelScope.launch {
        _personTVShowCredits.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.providePersonTVShowCredits(personId)
                _personTVShowCredits.postValue(handleResponse(response))
            } else {
                _personTVShowCredits.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _personTVShowCredits.postValue(Resource.Error("Network Failure"))
                else -> _personTVShowCredits.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getMovieCredits(movieId: Int) = viewModelScope.launch {
        _movieCredits.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideMovieCredits(movieId)
                _movieCredits.postValue(handleResponse(response))
            } else {
                _movieCredits.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _movieCredits.postValue(Resource.Error("Network Failure"))
                else -> _movieCredits.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getTVShowCredits(tvShowId: Int) = viewModelScope.launch {
        _tvShowCredits.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideTVShowCredits(tvShowId)
                _tvShowCredits.postValue(handleResponse(response))
            } else {
                _tvShowCredits.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _tvShowCredits.postValue(Resource.Error("Network Failure"))
                else -> _tvShowCredits.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getPersonDetails(personId: Int) = viewModelScope.launch {
        _person.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.providePersonDetails(personId)
                _person.postValue(handleResponse(response))
            } else {
                _person.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _person.postValue(Resource.Error("Network Failure"))
                else -> _person.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        _movie.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideMovieDetails(movieId)
                _movie.postValue(handleResponse(response))
            } else {
                _movie.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _movie.postValue(Resource.Error("Network Failure"))
                else -> _movie.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getSeriesDetails(tvId: Int) = viewModelScope.launch {
        _serie.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideSerieDetails(tvId)
                _serie.postValue(handleResponse(response))
            } else {
                _serie.postValue(Resource.Error("No internet connection"))
            }
        } catch (error: Throwable) {
            when(error) {
                is IOException -> _serie.postValue(Resource.Error("Network Failure"))
                else -> _serie.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}