package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.presentation.compose.PostImage

internal val PostImageSize = 56.dp
internal val PostItemPadding = 16.dp

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
                .padding(PostItemPadding),
        horizontalArrangement = Arrangement.spacedBy(PostItemPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PostImage(url = post.imageUrl, size = PostImageSize)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun PostListItemPreview() {
    NgamingCaseTheme {
        Surface {
            PostListItem(
                post =
                    Post(
                        id = 1,
                        title = "sunt aut facere repellat provident occaecati excepturi optio",
                        body = "quia et suscipit suscipit recusandae consequuntur expedita et cum",
                    ),
                onClick = {},
            )
        }
    }
}
