package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.component.topbar.NgamingTopBar
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@Composable
internal fun PostListTopBar(modifier: Modifier = Modifier) {
    NgamingTopBar(title = stringResource(R.string.post_list_title), modifier = modifier)
}
