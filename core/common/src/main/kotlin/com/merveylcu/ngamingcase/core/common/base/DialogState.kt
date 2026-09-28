package com.merveylcu.ngamingcase.core.common.base

import com.merveylcu.ngamingcase.core.common.UiText

public class DialogState(
    public val message: UiText,
    public val confirmText: UiText,
    public val onConfirm: () -> Unit,
    public val onDismiss: () -> Unit,
    public val title: UiText? = null,
    public val dismissText: UiText? = null,
)
