package com.merveylcu.ngamingcase.navigation.ext

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

public fun NavBackStack<NavKey>.navigateTo(destination: NavKey) {
    add(destination)
}

public fun NavBackStack<NavKey>.navigateBack(): NavKey? = removeLastOrNull()
