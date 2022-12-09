package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.abb.tap.cinephilia.data.network.tmdb.model.topratedmovies.TopRatedMoviesResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mediaRepository: MediaRepository) : ViewModel() {

    val topRatedMovies: MutableLiveData<Resource<TopRatedMoviesResponse>> = MutableLiveData()

    init {
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        topRatedMovies.postValue(Resource.Loading())
        val response = mediaRepository.provideTopRatedMovies()
        if (response.isSuccessful) {
            topRatedMovies.postValue(handleMoviesResponse(response))
        }
    }

    private fun handleMoviesResponse(response: Response<TopRatedMoviesResponse>): Resource<TopRatedMoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}