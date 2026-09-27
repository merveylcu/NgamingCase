package com.merveylcu.ngamingcase.feature.posts.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object PostListDestination : NavKey

@Serializable
data class PostDetailDestination(val id: Int) : NavKey
