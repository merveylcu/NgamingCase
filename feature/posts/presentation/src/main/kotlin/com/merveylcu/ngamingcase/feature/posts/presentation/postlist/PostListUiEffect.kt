package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity

sealed interface PostListUiEffect {
    data class ShowUndoDelete(val postId: Int) : PostListUiEffect
    data class ShowError(val error: ErrorEntity) : PostListUiEffect
}
