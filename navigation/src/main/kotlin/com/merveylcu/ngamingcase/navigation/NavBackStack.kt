package com.merveylcu.ngamingcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
public fun rememberNgamingCaseBackStack(start: NavKey = PostListDestination): NavBackStack<NavKey> = rememberNavBackStack(start)
