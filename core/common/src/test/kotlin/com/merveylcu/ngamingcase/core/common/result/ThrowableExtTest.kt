package com.merveylcu.ngamingcase.core.common.result

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class ThrowableExtTest {
    @Test
    fun `timeout maps to timeout`() {
        assertThat(
            SocketTimeoutException().toErrorEntity(),
        ).isEqualTo(network(ErrorEntity.Network.NetworkReason.TIMEOUT))
    }

    @Test
    fun `unknown host and refused connection map to no internet`() {
        val expected = network(ErrorEntity.Network.NetworkReason.NO_INTERNET)
        assertThat(UnknownHostException().toErrorEntity()).isEqualTo(expected)
        assertThat(ConnectException().toErrorEntity()).isEqualTo(expected)
    }

    @Test
    fun `ssl failure maps to ssl`() {
        assertThat(
            SSLException("handshake").toErrorEntity(),
        ).isEqualTo(network(ErrorEntity.Network.NetworkReason.SSL))
    }

    @Test
    fun `socket failure maps to connection`() {
        assertThat(
            SocketException().toErrorEntity(),
        ).isEqualTo(network(ErrorEntity.Network.NetworkReason.CONNECTION))
    }

    @Test
    fun `other io failure maps to unknown network error`() {
        assertThat(
            IOException().toErrorEntity(),
        ).isEqualTo(network(ErrorEntity.Network.NetworkReason.UNKNOWN))
    }

    @Test
    fun `non io failure maps to unknown`() {
        assertThat(IllegalStateException().toErrorEntity()).isEqualTo(ErrorEntity.Unknown)
    }

    private fun network(reason: ErrorEntity.Network.NetworkReason) = ErrorEntity.Network(reason)
}
