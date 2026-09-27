package com.merveylcu.ngamingcase.network.extensions

import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class NetworkExtensionsTest {

    @Test
    fun `successful call returns success`() = runTest {
        assertThat(safeApiCall { "value" }).isEqualTo(RestResult.Success("value"))
    }

    @Test
    fun `http failure maps to http error`() = runTest {
        val result = safeApiCall<Unit> { throw HttpException(Response.error<Any>(404, "".toResponseBody())) }

        assertThat(result).isEqualTo(RestResult.Error(ErrorEntity.Http(404)))
    }

    @Test
    fun `io failure maps to network error`() = runTest {
        val result = safeApiCall<Unit> { throw SocketTimeoutException() }

        assertThat(result).isEqualTo(RestResult.Error(ErrorEntity.Network(ErrorEntity.Network.NetworkReason.TIMEOUT)))
    }

    @Test
    fun `parse failure maps to unknown error`() = runTest {
        val result = safeApiCall<Unit> { throw SerializationException("bad json") }

        assertThat(result).isEqualTo(RestResult.Error(ErrorEntity.Unknown))
    }

    @Test
    fun `cancellation is not swallowed`() {
        assertThrows(CancellationException::class.java) {
            runBlocking { safeApiCall<Unit> { throw CancellationException("cancelled") } }
        }
    }

    @Test
    fun `unexpected failure is not swallowed`() {
        assertThrows(IllegalStateException::class.java) {
            runBlocking { safeApiCall<Unit> { error("bug") } }
        }
    }
}
