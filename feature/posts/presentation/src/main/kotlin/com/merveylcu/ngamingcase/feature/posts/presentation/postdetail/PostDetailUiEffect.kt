package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import com.merveylcu.ngamingcase.core.common.base.UiEffect

sealed interface PostDetailUiEffect : UiEffect {
    data object NavigateBack : PostDetailUiEffect
}
