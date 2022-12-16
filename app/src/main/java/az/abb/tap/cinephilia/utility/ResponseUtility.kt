package az.abb.tap.cinephilia.utility

import retrofit2.Response

fun <T: Any> handleResponse(response: Response<T>): Resource<T> {
    if (response.isSuccessful) {
        response.body()?.let { resultResponse ->
            return Resource.Success(resultResponse)
        }
    }
    return Resource.Error(response.message())
}