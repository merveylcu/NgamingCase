package com.merveylcu.ngamingcase.core.ui.extension

import androidx.annotation.StringRes
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.ui.R

@StringRes
public fun ErrorEntity.toMessageRes(): Int = when (this) {
    is ErrorEntity.Network -> when (reason) {
        ErrorEntity.Network.NetworkReason.TIMEOUT -> R.string.error_timeout
        ErrorEntity.Network.NetworkReason.NO_INTERNET -> R.string.error_no_internet
        else -> R.string.error_connection
    }

    is ErrorEntity.Http -> R.string.error_server

    ErrorEntity.Unknown -> R.string.error_unknown
}
