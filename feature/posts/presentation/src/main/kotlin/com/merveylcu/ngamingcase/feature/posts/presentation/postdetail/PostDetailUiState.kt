package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import com.merveylcu.ngamingcase.feature.posts.domain.model.Post

data class PostDetailUiState(
    val post: Post? = null,
    val title: String = "",
    val body: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val notFound: Boolean = false,
    val isDiscardDialogVisible: Boolean = false,
) {
    val isTitleValid: Boolean get() = title.isNotBlank()
    val isDirty: Boolean get() = post != null && (title != post.title || body != post.body)
    val canSave: Boolean get() = isTitleValid && isDirty && !isSaving
}
