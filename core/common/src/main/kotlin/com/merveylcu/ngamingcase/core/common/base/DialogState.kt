package com.merveylcu.ngamingcase.core.common.base

import com.merveylcu.ngamingcase.core.common.UiText

public class DialogState(
    public val title: UiText,
    public val message: UiText,
    public val confirmText: UiText,
    public val dismissText: UiText,
    public val onConfirm: () -> Unit,
    public val onDismiss: () -> Unit,
)
