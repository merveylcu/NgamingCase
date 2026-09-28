package com.merveylcu.ngamingcase.core.common.base

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity

public sealed interface BaseUiState<out S> {

    public data object Loading : BaseUiState<Nothing>

    public data class Error(val error: ErrorEntity) : BaseUiState<Nothing>

    public data class Content<out S>(
        val data: S,
        val isLoading: Boolean = false,
        val dialogState: DialogState? = null,
    ) : BaseUiState<S>
}
