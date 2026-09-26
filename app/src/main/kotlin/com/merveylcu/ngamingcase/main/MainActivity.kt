package com.merveylcu.ngamingcase.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.postdetail.PostDetailScreen
import com.merveylcu.ngamingcase.feature.posts.presentation.postlist.PostListScreen
import com.merveylcu.ngamingcase.navigation.NgamingCaseNavHost
import com.merveylcu.ngamingcase.navigation.PostDetailDestination
import com.merveylcu.ngamingcase.navigation.PostListDestination
import com.merveylcu.ngamingcase.navigation.ext.navigateBack
import com.merveylcu.ngamingcase.navigation.ext.navigateTo
import com.merveylcu.ngamingcase.navigation.rememberNgamingCaseBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NgamingCaseTheme {
                val backStack = rememberNgamingCaseBackStack()
                NgamingCaseNavHost(backStack = backStack) {
                    entry<PostListDestination> {
                        PostListScreen(onPostClick = { id -> backStack.navigateTo(PostDetailDestination(id)) })
                    }
                    entry<PostDetailDestination> { destination ->
                        PostDetailScreen(postId = destination.id, onBack = { backStack.navigateBack() })
                    }
                }
            }
        }
    }
}
