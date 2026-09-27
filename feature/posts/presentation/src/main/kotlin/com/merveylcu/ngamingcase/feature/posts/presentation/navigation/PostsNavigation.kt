package com.merveylcu.ngamingcase.feature.posts.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailScreen
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListScreen
import com.merveylcu.ngamingcase.navigation.Navigator

fun EntryProviderScope<NavKey>.postsEntries(navigator: Navigator) {
    entry<PostListDestination> {
        PostListScreen(onPostClick = { id -> navigator.navigateTo(PostDetailDestination(id)) })
    }
    entry<PostDetailDestination> { destination ->
        PostDetailScreen(postId = destination.id, onBack = navigator::navigateBack)
    }
}
