package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import com.merveylcu.ngamingcase.core.common.base.UiEffect
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity

sealed interface PostDetailUiEffect : UiEffect {
    data object NavigateBack : PostDetailUiEffect
    data class ShowError(val error: ErrorEntity) : PostDetailUiEffect
}
