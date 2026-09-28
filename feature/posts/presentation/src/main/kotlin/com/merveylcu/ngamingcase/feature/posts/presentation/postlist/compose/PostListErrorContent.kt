package com.merveylcu.ngamingcase.feature.posts.presentation.postlist.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes
import com.merveylcu.ngamingcase.core.designsystem.R as DesignR

@Composable
internal fun PostListErrorContent(
    error: ErrorEntity,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(NgamingCaseTheme.dimens.spacingXl),
        verticalArrangement = Arrangement.spacedBy(
            NgamingCaseTheme.dimens.spacingMd,
            Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(DesignR.drawable.ic_error),
            contentDescription = null,
            tint = NgamingCaseTheme.colors.error,
            modifier = Modifier.size(NgamingCaseTheme.iconSizes.large),
        )
        Text(
            text = stringResource(error.toMessageRes()),
            style = NgamingCaseTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(DesignR.string.base_retry))
        }
    }
}
