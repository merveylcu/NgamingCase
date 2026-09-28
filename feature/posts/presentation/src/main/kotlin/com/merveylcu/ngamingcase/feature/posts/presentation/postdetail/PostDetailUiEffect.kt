package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity

sealed interface PostDetailUiEffect {
    data object NavigateBack : PostDetailUiEffect

    data class ShowError(val error: ErrorEntity) : PostDetailUiEffect
}
