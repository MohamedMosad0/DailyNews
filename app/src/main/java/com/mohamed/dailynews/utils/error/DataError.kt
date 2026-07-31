package com.mohamed.dailynews.utils.error

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface DataError {
    data object Offline : DataError
    data object NoCache : DataError
    data class Server(val code: Int, val message: String) : DataError
    data object Timeout : DataError
    data class Unknown(val message: String, val cause: Throwable? = null) : DataError
}

class DataException(
    val error: DataError
) : Exception("Data error occurred: $error")

fun Throwable.toDataError(): DataError = when (this) {
    is DataException -> this.error
    is UnknownHostException -> DataError.Offline
    is SocketTimeoutException -> DataError.Timeout
    is HttpException -> DataError.Server(code(), message())
    is IOException -> DataError.Offline
    else -> DataError.Unknown(message ?: "Unknown error", this)
}
