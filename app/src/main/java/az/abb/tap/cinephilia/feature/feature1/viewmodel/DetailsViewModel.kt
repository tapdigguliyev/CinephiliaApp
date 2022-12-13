package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.abb.tap.cinephilia.data.network.tmdb.model.moviedetailsresponse.MovieDetailsResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.utility.NetworkStatusChecker
import az.abb.tap.cinephilia.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val networkStatusChecker: NetworkStatusChecker
) : ViewModel() {

    private val _movie: MutableLiveData<Resource<MovieDetailsResponse>> = MutableLiveData()
    val movie: LiveData<Resource<MovieDetailsResponse>> = _movie

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        _movie.postValue(Resource.Loading())
        try {
            if (networkStatusChecker.hasInternetConnection()) {
                val response = mediaRepository.provideMovieDetails(movieId)
                _movie.postValue(handleMoviesResponse(response))
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

    private fun handleMoviesResponse(response: Response<MovieDetailsResponse>): Resource<MovieDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}