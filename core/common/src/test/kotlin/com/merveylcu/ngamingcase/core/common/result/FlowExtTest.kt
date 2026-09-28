package com.merveylcu.ngamingcase.core.common.result

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class FlowExtTest {

    @Test
    fun `result flow wraps the result in loading states`() = runTest {
        val results = resultFlow { RestResult.Success(1) }.toList()

        assertThat(results).containsExactly(
            RestResult.Loading(true),
            RestResult.Success(1),
            RestResult.Loading(false),
        ).inOrder()
    }

    @Test
    fun `unexpected failure is mapped to an error and loading still ends`() = runTest {
        val results = flow<RestResult<Int>> {
            throw UnknownHostException()
        }.buildDefaultFlow().toList()

        assertThat(results).containsExactly(
            RestResult.Loading(true),
            RestResult.Error(ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)),
            RestResult.Loading(false),
        ).inOrder()
    }

    @Test
    fun `loading can be turned off`() = runTest {
        val results = flow {
            emit(RestResult.Success(1))
        }.buildDefaultFlow(showLoading = false).toList()

        assertThat(results).containsExactly(RestResult.Success(1))
    }
}
