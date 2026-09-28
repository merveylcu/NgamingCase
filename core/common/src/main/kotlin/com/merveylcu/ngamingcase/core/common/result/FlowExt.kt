package com.merveylcu.ngamingcase.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

public fun <T> Flow<RestResult<T>>.buildDefaultFlow(
    showLoading: Boolean = true,
): Flow<RestResult<T>> = onStart { if (showLoading) emit(RestResult.Loading(true)) }
    .catch { emit(RestResult.Error(it.toErrorEntity())) }
    .onCompletion { if (showLoading) emit(RestResult.Loading(false)) }

public fun <T> resultFlow(block: suspend () -> RestResult<T>): Flow<RestResult<T>> =
    flow { emit(block()) }.buildDefaultFlow()
