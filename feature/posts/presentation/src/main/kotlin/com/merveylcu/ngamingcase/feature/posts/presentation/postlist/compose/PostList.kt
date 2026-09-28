package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PostList(
    posts: ImmutableList<Post>,
    onPostClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnPostClick by rememberUpdatedState(onPostClick)
    val currentOnDelete by rememberUpdatedState(onDelete)
    val listState = rememberLazyListState()
    val dimens = NgamingCaseTheme.dimens
    val dividerStartPadding = dimens.spacingMd * 2 + dimens.imageSm

    RevealRestoredPostEffect(posts = posts, listState = listState)

    LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
        itemsIndexed(items = posts, key = { _, post -> post.id }) { index, post ->
            Column(modifier = Modifier.animateItem(fadeOutSpec = null)) {
                PostListSwipeToDeleteContainer(onDelete = { currentOnDelete(post.id) }) {
                    PostListItem(
                        post = post,
                        onClick = { currentOnPostClick(post.id) },
                        modifier = Modifier.background(NgamingCaseTheme.colors.surface),
                    )
                }
                if (index < posts.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(start = dividerStartPadding))
                }
            }
        }
    }
}

@Composable
private fun RevealRestoredPostEffect(posts: ImmutableList<Post>, listState: LazyListState) {
    val previousIds = remember { mutableSetOf<Int>() }
    LaunchedEffect(posts) {
        val insertedIndex = if (previousIds.isEmpty()) {
            -1
        } else {
            posts.indexOfFirst { it.id !in previousIds }
        }
        previousIds.clear()
        previousIds.addAll(posts.map { it.id })
        withFrameNanos { }
        if (insertedIndex in 0 until listState.firstVisibleItemIndex) {
            listState.animateScrollToItem(insertedIndex)
        }
    }
}
