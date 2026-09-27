package com.merveylcu.ngamingcase.core.common.result

import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.PortUnreachableException
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

public fun Throwable.toErrorEntity(): ErrorEntity = when (this) {
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

    is IOException ->
        ErrorEntity.Network(ErrorEntity.Network.NetworkReason.UNKNOWN)

    else -> ErrorEntity.Unknown
}
