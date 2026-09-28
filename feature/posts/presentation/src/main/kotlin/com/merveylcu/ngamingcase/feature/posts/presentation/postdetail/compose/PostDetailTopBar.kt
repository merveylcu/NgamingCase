package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingTextButton
import com.merveylcu.ngamingcase.core.designsystem.component.progress.NgamingSmallProgressIndicator
import com.merveylcu.ngamingcase.core.designsystem.component.topbar.NgamingTopBar
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.R

@Composable
internal fun PostDetailTopBar(
    isSaving: Boolean,
    showSave: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NgamingTopBar(
        title = stringResource(R.string.post_detail_title),
        modifier = modifier,
        onBack = onBack,
        actions = {
            if (isSaving) {
                NgamingSmallProgressIndicator(
                    modifier = Modifier.padding(horizontal = NgamingCaseTheme.spacing.md),
                )
            } else if (showSave) {
                NgamingTextButton(
                    text = stringResource(R.string.post_detail_save),
                    onClick = onSave,
                    enabled = canSave,
                )
            }
        },
    )
}
