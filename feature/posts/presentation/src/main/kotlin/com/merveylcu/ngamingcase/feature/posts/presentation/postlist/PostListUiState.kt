package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PostListUiState(
    val posts: ImmutableList<Post> = persistentListOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: ErrorEntity? = null,
)
