package com.merveylcu.ngamingcase.navigation

import androidx.navigation3.runtime.NavKey

public interface Navigator {
    public fun navigateTo(destination: NavKey)

    public fun navigateBack()
}
