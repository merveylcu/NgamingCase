package com.merveylcu.ngamingcase.navigation

import androidx.navigation3.runtime.NavKey
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BackStackNavigatorTest {

    private data object Start : NavKey

    private data class Detail(val id: Int) : NavKey

    @Test
    fun `navigate to pushes the destination`() {
        val backStack = mutableListOf<NavKey>(Start)

        BackStackNavigator(backStack).navigateTo(Detail(1))

        assertThat(backStack).containsExactly(Start, Detail(1)).inOrder()
    }

    @Test
    fun `navigate back pops the top destination`() {
        val backStack = mutableListOf<NavKey>(Start, Detail(1))

        BackStackNavigator(backStack).navigateBack()

        assertThat(backStack).containsExactly(Start)
    }

    @Test
    fun `navigate back keeps the start destination`() {
        val backStack = mutableListOf<NavKey>(Start)

        BackStackNavigator(backStack).navigateBack()

        assertThat(backStack).containsExactly(Start)
    }
}
