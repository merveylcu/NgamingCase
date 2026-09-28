package com.merveylcu.ngamingcase.core.common.result

public sealed interface RestResult<out T> {
    public data class Success<out T>(val data: T) : RestResult<T>

    public data class Error(val error: ErrorEntity) : RestResult<Nothing>
}

public inline fun <T, R> RestResult<T>.mapOnSuccess(transform: (T) -> R): RestResult<R> =
    when (this) {
        is RestResult.Success -> RestResult.Success(transform(data))
        is RestResult.Error -> this
    }
