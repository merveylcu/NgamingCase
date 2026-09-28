package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostListTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.post_list_title)) },
        modifier = modifier,
    )
}
