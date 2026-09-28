package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
            .padding(NgamingCaseTheme.dimens.spacingMd),
        verticalArrangement = Arrangement.spacedBy(NgamingCaseTheme.dimens.spacingMd),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PostImage(url = imageUrl, size = NgamingCaseTheme.dimens.imageLg)
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(text = stringResource(R.string.post_detail_title_label)) },
            singleLine = true,
            enabled = enabled,
            isError = !isTitleValid,
            supportingText = if (isTitleValid) {
                null
            } else {
                { Text(text = stringResource(R.string.post_detail_title_empty)) }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = body,
            onValueChange = onBodyChange,
            label = { Text(text = stringResource(R.string.post_detail_body_label)) },
            minLines = BODY_MIN_LINES,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
