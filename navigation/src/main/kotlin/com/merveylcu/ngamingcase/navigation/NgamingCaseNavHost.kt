package com.merveylcu.ngamingcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
public fun NgamingCaseNavHost(
    startDestination: NavKey,
    modifier: Modifier = Modifier,
    entries: EntryProviderScope<NavKey>.(Navigator) -> Unit,
) {
    val backStack = rememberNavBackStack(startDestination)
    val navigator = remember(backStack) { BackStackNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = navigator::navigateBack,
        entryProvider = entryProvider { entries(navigator) },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
    )
}
