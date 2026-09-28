package com.merveylcu.ngamingcase.core.designsystem.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.merveylcu.ngamingcase.core.designsystem.R
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingButton
import com.merveylcu.ngamingcase.core.designsystem.component.icon.NgamingIcon
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme

@Composable
fun NgamingErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(NgamingCaseTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(
            NgamingCaseTheme.spacing.md,
            Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NgamingIcon(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            tint = NgamingCaseTheme.colors.error,
            modifier = Modifier.size(NgamingCaseTheme.iconSizes.large),
        )
        NgamingText(
            text = message,
            style = NgamingCaseTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        if (onRetry != null) {
            NgamingButton(text = stringResource(R.string.base_retry), onClick = onRetry)
        }
    }
}
