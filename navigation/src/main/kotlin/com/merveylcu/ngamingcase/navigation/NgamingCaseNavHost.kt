package com.merveylcu.ngamingcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

/**
 * Each entry gets its own ViewModelStore, so a screen's ViewModel is cleared when it is popped.
 */
@Composable
public fun NgamingCaseNavHost(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    entryProviderBuilder: EntryProviderScope<NavKey>.() -> Unit,
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider(builder = entryProviderBuilder),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
    )
}
