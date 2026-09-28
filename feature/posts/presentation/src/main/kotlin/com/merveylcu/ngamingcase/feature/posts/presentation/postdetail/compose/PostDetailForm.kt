package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.component.textfield.NgamingTextField
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.R
import com.merveylcu.ngamingcase.feature.posts.presentation.compose.PostImage

private const val BODY_MIN_LINES = 4

@Composable
internal fun PostDetailForm(
    imageUrl: String,
    title: String,
    body: String,
    isTitleValid: Boolean,
    enabled: Boolean,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(NgamingCaseTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(NgamingCaseTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PostImage(url = imageUrl, size = NgamingCaseTheme.dimens.imageLg)
        NgamingTextField(
            value = title,
            onValueChange = onTitleChange,
            label = stringResource(R.string.post_detail_title_label),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            errorText = if (isTitleValid) {
                null
            } else {
                stringResource(
                    R.string.post_detail_title_empty,
                )
            },
        )
        NgamingTextField(
            value = body,
            onValueChange = onBodyChange,
            label = stringResource(R.string.post_detail_body_label),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            minLines = BODY_MIN_LINES,
        )
    }
}
