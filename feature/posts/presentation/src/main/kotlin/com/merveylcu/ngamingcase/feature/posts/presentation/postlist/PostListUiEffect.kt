package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import com.merveylcu.ngamingcase.core.common.base.UiEffect

sealed interface PostListUiEffect : UiEffect {
    data class ShowUndoDelete(val postId: Int) : PostListUiEffect
}
