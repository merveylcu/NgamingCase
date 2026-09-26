package com.merveylcu.ngamingcase.core.designsystem.extension

import androidx.annotation.StringRes
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.designsystem.R

@StringRes
fun ErrorEntity.toMessageRes(): Int = when (this) {
    is ErrorEntity.Network -> when (reason) {
        ErrorEntity.Network.NetworkReason.TIMEOUT -> R.string.base_error_timeout
        ErrorEntity.Network.NetworkReason.NO_INTERNET -> R.string.base_error_no_internet
        else -> R.string.base_error_connection
    }

    is ErrorEntity.Http -> R.string.base_error_server

    ErrorEntity.Unknown -> R.string.base_error_unknown
}
