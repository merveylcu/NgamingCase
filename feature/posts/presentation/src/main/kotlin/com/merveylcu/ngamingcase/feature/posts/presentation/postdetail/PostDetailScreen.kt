package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import com.merveylcu.ngamingcase.feature.posts.presentation.compose.PostImage
import com.merveylcu.ngamingcase.core.designsystem.R as DesignR

private val DetailImageSize = 120.dp
private const val BODY_MIN_LINES = 4

@Composable
fun PostDetailScreen(
    postId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostDetailViewModel =
        hiltViewModel<PostDetailViewModel, PostDetailViewModel.Factory>(
            key = postId.toString(),
            creationCallback = { factory -> factory.create(postId) },
        ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val currentOnBack by rememberUpdatedState(onBack)

    LaunchedEffect(viewModel, snackbarHostState) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                PostDetailUiEffect.NavigateBack -> {
                    currentOnBack()
                }

                is PostDetailUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(resources.getString(effect.error.toMessageRes()))
                }
            }
        }
    }

    BackHandler(onBack = viewModel::onBack)

    PostDetailContent(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onBack = viewModel::onBack,
        onSave = viewModel::onSave,
        onTitleChange = viewModel::onTitleChange,
        onBodyChange = viewModel::onBodyChange,
        onDiscardConfirm = viewModel::onDiscardConfirm,
        onDiscardDismiss = viewModel::onDiscardDismiss,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailContent(
    state: PostDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDiscardConfirm: () -> Unit,
    onDiscardDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PostDetailTopBar(
                isSaving = state.isSaving,
                showSave = state.post != null,
                canSave = state.canSave,
                onBack = onBack,
                onSave = onSave,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val contentModifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        when {
            state.isLoading -> {
                Box(contentModifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.notFound || state.post == null -> {
                Box(
                    modifier = contentModifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.post_detail_not_found),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                PostDetailForm(
                    post = state.post,
                    state = state,
                    onTitleChange = onTitleChange,
                    onBodyChange = onBodyChange,
                    modifier = contentModifier,
                )
            }
        }
    }

    if (state.isDiscardDialogVisible) {
        PostDetailDiscardDialog(onConfirm = onDiscardConfirm, onDismiss = onDiscardDismiss)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostDetailTopBar(
    isSaving: Boolean,
    showSave: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.post_detail_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_arrow_back),
                    contentDescription = stringResource(DesignR.string.base_back),
                )
            }
        },
        actions = {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .size(24.dp),
                    strokeWidth = 2.dp,
                )
            } else if (showSave) {
                TextButton(onClick = onSave, enabled = canSave) {
                    Text(text = stringResource(R.string.post_detail_save))
                }
            }
        },
    )
}

@Composable
private fun PostDetailDiscardDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.post_detail_discard_title)) },
        text = { Text(text = stringResource(R.string.post_detail_discard_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.post_detail_discard_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.post_detail_discard_dismiss))
            }
        },
    )
}

@Composable
private fun PostDetailForm(
    post: Post,
    state: PostDetailUiState,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PostImage(url = post.imageUrl, size = DetailImageSize)
        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChange,
            label = { Text(text = stringResource(R.string.post_detail_title_label)) },
            singleLine = true,
            enabled = !state.isSaving,
            isError = !state.isTitleValid,
            supportingText =
                if (state.isTitleValid) {
                    null
                } else {
                    { Text(text = stringResource(R.string.post_detail_title_empty)) }
                },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.body,
            onValueChange = onBodyChange,
            label = { Text(text = stringResource(R.string.post_detail_body_label)) },
            minLines = BODY_MIN_LINES,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val previewPost =
    Post(
        id = 1,
        title = "sunt aut facere repellat provident occaecati",
        body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
    )

@Preview
@Composable
private fun PostDetailContentPreview() {
    NgamingCaseTheme {
        PostDetailContent(
            state =
                PostDetailUiState(
                    post = previewPost,
                    title = previewPost.title,
                    body = previewPost.body,
                    isLoading = false,
                ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSave = {},
            onTitleChange = {},
            onBodyChange = {},
            onDiscardConfirm = {},
            onDiscardDismiss = {},
        )
    }
}

@Preview
@Composable
private fun PostDetailContentEmptyTitlePreview() {
    NgamingCaseTheme {
        PostDetailContent(
            state =
                PostDetailUiState(
                    post = previewPost,
                    title = "",
                    body = previewPost.body,
                    isLoading = false,
                ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onSave = {},
            onTitleChange = {},
            onBodyChange = {},
            onDiscardConfirm = {},
            onDiscardDismiss = {},
        )
    }
}
