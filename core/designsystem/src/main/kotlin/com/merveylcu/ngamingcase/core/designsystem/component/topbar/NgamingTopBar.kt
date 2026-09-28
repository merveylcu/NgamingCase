package com.merveylcu.ngamingcase.core.designsystem.component.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.designsystem.R
import com.merveylcu.ngamingcase.core.designsystem.component.button.NgamingIconButton
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NgamingTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { NgamingText(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (onBack != null) {
                NgamingIconButton(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.base_back),
                    onClick = onBack,
                )
            }
        },
        actions = actions,
    )
}
