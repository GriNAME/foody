package com.griname.foody.util

sealed class NetworkResult<T>(
    val message: String? = null,
    val data: T? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(message, data)
    class Loading<T>() : NetworkResult<T>()
}
