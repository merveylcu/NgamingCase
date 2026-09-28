package com.merveylcu.ngamingcase.core.designsystem.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.merveylcu.ngamingcase.core.common.UiText

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.StringResource -> stringResource(id = id, formatArgs = args)
}
