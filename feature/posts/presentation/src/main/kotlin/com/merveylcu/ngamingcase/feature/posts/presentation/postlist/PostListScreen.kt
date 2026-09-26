package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.designsystem.extension.toMessageRes
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostImageSize
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostItemPadding
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostListEmptyContent
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostListErrorContent
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose.PostListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PostListScreen(
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PostListUiEffect.ShowError ->
                    snackbarHostState.showSnackbar(resources.getString(effect.error.toMessageRes()))
            }
        }
    }

    PostListContent(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onRefresh = viewModel::onRefresh,
        onRetry = viewModel::onRetry,
        onPostClick = onPostClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostListContent(
    state: PostListUiState,
    snackbarHostState: SnackbarHostState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.post_list_title)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.posts.isEmpty() && state.isLoading -> Box(contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            state.posts.isEmpty() && state.error != null -> PostListErrorContent(
                error = state.error,
                onRetry = onRetry,
                modifier = contentModifier,
            )

            else -> PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = contentModifier,
            ) {
                if (state.posts.isEmpty()) {
                    PostListEmptyContent()
                } else {
                    PostList(posts = state.posts, onPostClick = onPostClick)
                }
            }
        }
    }
}

@Composable
private fun PostList(
    posts: ImmutableList<Post>,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnPostClick by rememberUpdatedState(onPostClick)
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(items = posts, key = { _, post -> post.id }) { index, post ->
            Column(modifier = Modifier.animateItem()) {
                PostListItem(post = post, onClick = { currentOnPostClick(post.id) })
                if (index < posts.lastIndex) {
                    // Starts where the text starts, after the image.
                    HorizontalDivider(modifier = Modifier.padding(start = PostItemPadding * 2 + PostImageSize))
                }
            }
        }
    }
}

private val previewPosts = List(5) { index ->
    Post(id = index + 1, title = "Post title ${index + 1}", body = "Short description of the post number ${index + 1}.")
}.toImmutableList()

@Preview
@Composable
private fun PostListContentPreview() {
    NgamingCaseTheme {
        PostListContent(
            state = PostListUiState(posts = previewPosts),
            snackbarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onRetry = {},
            onPostClick = {},
        )
    }
}

@Preview
@Composable
private fun PostListContentEmptyPreview() {
    NgamingCaseTheme {
        PostListContent(
            state = PostListUiState(posts = persistentListOf()),
            snackbarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onRetry = {},
            onPostClick = {},
        )
    }
}

@Preview
@Composable
private fun PostListContentErrorPreview() {
    NgamingCaseTheme {
        PostListContent(
            state = PostListUiState(error = ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)),
            snackbarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onRetry = {},
            onPostClick = {},
        )
    }
}
