package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@Composable
internal fun PostListEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.post_list_empty),
            style = NgamingCaseTheme.typography.bodyLarge,
            color = NgamingCaseTheme.colors.onSurfaceVariant,
        )
    }
}
