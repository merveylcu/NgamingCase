package com.merveylcu.ngamingcase.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.presentation.navigation.PostListDestination
import com.merveylcu.ngamingcase.feature.posts.presentation.navigation.postsEntries
import com.merveylcu.ngamingcase.navigation.NgamingCaseNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NgamingCaseTheme {
                NgamingCaseNavHost(startDestination = PostListDestination) { navigator ->
                    postsEntries(navigator)
                }
            }
        }
    }
}
