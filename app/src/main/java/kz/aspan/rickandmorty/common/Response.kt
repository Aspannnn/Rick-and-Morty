package kz.aspan.rickandmorty.common

import android.accounts.NetworkErrorException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val UNAUTHORIZED = 401

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Nothing>() {

        val isNetworkError = exception is NetworkErrorException
                || exception is SocketTimeoutException
                || exception is UnknownHostException
                || exception is ConnectException
                || exception is IOException

//        val isApiException = exception is ApiException

        val isSessionExpired = exception is HttpException && exception.code() == UNAUTHORIZED

        val isHttpException = exception is HttpException

//        val apiError: ApiError?
//            get() = (this.exception as? ApiException)?.response?.error
    }

    data class Loading(val isLoading: Boolean = true) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }

    val isSuccessful: Boolean
        get() = this is Success

    fun <T> transform(data: (R) -> T): Response<T> {
        if (this is Success) {
            return Success(data.invoke(this.data))
        }
        return this as Error
    }
}

//class ApiException(val response: ApiResponse<*>? = null) : Exception(response?.message)
//
//class VerificationException(val verification: Verification) : Exception()