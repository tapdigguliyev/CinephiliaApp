package az.abb.tap.cinephilia.feature.feature1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.abb.tap.cinephilia.data.network.tmdb.model.movieresponse.MoviesResponse
import az.abb.tap.cinephilia.data.repository.MediaRepository
import az.abb.tap.cinephilia.feature.feature1.model.genres.Genre
import az.abb.tap.cinephilia.utility.Resource
import az.abb.tap.cinephilia.utility.toGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mediaRepository: MediaRepository) : ViewModel() {

    private val _topRatedMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val topRatedMovies: LiveData<Resource<MoviesResponse>> = _topRatedMovies

    private val _popularMovies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    val popularMovies: LiveData<Resource<MoviesResponse>> = _popularMovies

    var movieGenres: MutableList<Genre> = mutableListOf()

    init {
        getTopRatedMovies()
        getPopularMovies()
        getGenres()
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        _topRatedMovies.postValue(Resource.Loading())
        val response = mediaRepository.provideTopRatedMovies()
        _topRatedMovies.postValue(handleMoviesResponse(response))
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _popularMovies.postValue(Resource.Loading())
        val response = mediaRepository.providePopularMovies()
        _popularMovies.postValue(handleMoviesResponse(response))
    }

    private fun handleMoviesResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun getGenres() = viewModelScope.launch {
        val response = mediaRepository.provideMovieGenres()
        if (response.isSuccessful) {
            movieGenres.addAll(response.body()?.genres?.map { it.toGenre() }!!)
        }
    }
}