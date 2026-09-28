package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@Composable
internal fun PostDetailNotFoundContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.post_detail_not_found),
            style = NgamingCaseTheme.typography.bodyLarge,
            color = NgamingCaseTheme.colors.onSurfaceVariant,
        )
    }
}
