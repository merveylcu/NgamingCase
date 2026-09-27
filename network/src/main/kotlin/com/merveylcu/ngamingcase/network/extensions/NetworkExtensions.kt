package com.merveylcu.ngamingcase.network.extensions

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.common.result.toErrorEntity
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

@Suppress("TooGenericExceptionCaught")
public suspend fun <T> safeApiCall(call: suspend () -> T): RestResult<T> = try {
    RestResult.Success(call())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    RestResult.Error(ErrorEntity.Http(e.code()))
} catch (e: Exception) {
    RestResult.Error(e.toErrorEntity())
}
