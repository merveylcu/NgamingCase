package com.merveylcu.ngamingcase.network.extensions

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.common.result.toErrorEntity
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

public suspend fun <T> safeApiCall(call: suspend () -> T): RestResult<T> = try {
    RestResult.Success(call())
} catch (e: HttpException) {
    RestResult.Error(ErrorEntity.Http(e.code()))
} catch (e: IOException) {
    RestResult.Error(e.toErrorEntity())
} catch (e: SerializationException) {
    RestResult.Error(e.toErrorEntity())
}
