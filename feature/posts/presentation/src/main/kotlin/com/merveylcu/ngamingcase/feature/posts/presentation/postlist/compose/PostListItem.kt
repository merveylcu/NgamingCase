package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.merveylcu.ngamingcase.core.designsystem.component.image.NgamingAsyncImage
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post

@Composable
internal fun PostListItem(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(NgamingCaseTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(NgamingCaseTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NgamingAsyncImage(url = post.imageUrl, size = NgamingCaseTheme.dimens.imageSm)
        Column(modifier = Modifier.weight(1f)) {
            NgamingText(
                text = post.title,
                style = NgamingCaseTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            NgamingText(
                text = post.body,
                style = NgamingCaseTheme.typography.bodyMedium,
                color = NgamingCaseTheme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
