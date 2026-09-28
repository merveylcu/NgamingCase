package com.merveylcu.ngamingcase.core.designsystem.component.textfield

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.merveylcu.ngamingcase.core.designsystem.component.text.NgamingText

@Composable
fun NgamingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    minLines: Int = 1,
    errorText: String? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = { NgamingText(text = label) },
        supportingText = errorText?.let { { NgamingText(text = it) } },
        isError = errorText != null,
        singleLine = singleLine,
        minLines = minLines,
    )
}
