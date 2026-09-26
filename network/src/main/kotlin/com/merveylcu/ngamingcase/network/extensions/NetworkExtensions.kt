package com.merveylcu.ngamingcase.network.extensions

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.PortUnreachableException
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs [call] and maps any failure to an [ErrorEntity]. Cancellation is never swallowed.
 */
@Suppress("TooGenericExceptionCaught")
public suspend fun <T> safeApiCall(call: suspend () -> T): RestResult<T> = try {
    RestResult.Success(call())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    RestResult.Error(e.toErrorEntity())
}

public fun Throwable.toErrorEntity(): ErrorEntity = when (this) {
    is HttpException -> ErrorEntity.Http(code())

    is SocketTimeoutException ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.TIMEOUT)

    is UnknownHostException,
    is ConnectException,
    ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)

    is SSLException ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.SSL)

    is ProtocolException,
    is PortUnreachableException,
    is NoRouteToHostException,
    is SocketException,
    is InterruptedIOException,
    ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.CONNECTION)

    is java.io.IOException ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.UNKNOWN)

    else -> ErrorEntity.Unknown
}
