package com.searchinrecyclerviewexample.model

sealed class BaseRes<T>(val data: Any? = null, val message: String? = null) {
    class Success<T>(data: Any) : BaseRes<T>(data)
    class Error<T>(message: String, data: T? = null) : BaseRes<T>(data, message)
    class Loading<T> : BaseRes<T>()
}