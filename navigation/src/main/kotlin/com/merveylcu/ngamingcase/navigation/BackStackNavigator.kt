package com.merveylcu.ngamingcase.navigation

import androidx.navigation3.runtime.NavKey

internal class BackStackNavigator(private val backStack: MutableList<NavKey>) : Navigator {
    override fun navigateTo(destination: NavKey) {
        backStack.add(destination)
    }

    override fun navigateBack() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}
