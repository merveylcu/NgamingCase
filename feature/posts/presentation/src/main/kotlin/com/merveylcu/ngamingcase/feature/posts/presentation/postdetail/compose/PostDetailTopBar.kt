package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import com.merveylcu.ngamingcase.core.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailTopBar(
    isSaving: Boolean,
    showSave: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.post_detail_title)) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_arrow_back),
                    contentDescription = stringResource(DesignR.string.base_back),
                )
            }
        },
        actions = {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(horizontal = NgamingCaseTheme.spacing.md)
                        .size(NgamingCaseTheme.iconSizes.medium),
                    strokeWidth = NgamingCaseTheme.dimens.progressStrokeWidth,
                )
            } else if (showSave) {
                TextButton(onClick = onSave, enabled = canSave) {
                    Text(text = stringResource(R.string.post_detail_save))
                }
            }
        },
    )
}
