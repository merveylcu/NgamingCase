package com.merveylcu.ngamingcase.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data object PostListDestination : NavKey

@Serializable
public data class PostDetailDestination(val id: Int) : NavKey
