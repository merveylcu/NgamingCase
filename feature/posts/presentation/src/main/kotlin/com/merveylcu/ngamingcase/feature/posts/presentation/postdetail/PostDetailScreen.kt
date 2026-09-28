package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose.PostDetailContent

@Composable
fun PostDetailScreen(
    postId: Int,
    onBack: () -> Unit,
    viewModel: PostDetailViewModel =
        hiltViewModel<PostDetailViewModel, PostDetailViewModel.Factory>(
            key = postId.toString(),
            creationCallback = { factory -> factory.create(postId) },
        ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnBack by rememberUpdatedState(onBack)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                PostDetailUiEffect.NavigateBack -> currentOnBack()
            }
        }
    }

    BackHandler(onBack = viewModel::onBack)

    PostDetailContent(
        state = uiState,
        onBack = viewModel::onBack,
        onSave = viewModel::onSave,
        onTitleChange = viewModel::onTitleChange,
        onBodyChange = viewModel::onBodyChange,
    )
}
