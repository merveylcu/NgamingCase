package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.serviceLoaderEnabled
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.designsystem.theme.NgamingCaseTheme
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @OptIn(DelicateCoilApi::class)
    @Before
    fun setUp() {
        SingletonImageLoader.setUnsafe(
            ImageLoader
                .Builder(ApplicationProvider.getApplicationContext())
                .serviceLoaderEnabled(false)
                .build(),
        )
    }

    private fun setContent(
        state: PostListUiState,
        onRetry: () -> Unit = {},
        onPostClick: (Int) -> Unit = {},
    ) {
        composeRule.setContent {
            NgamingCaseTheme {
                PostListContent(
                    state = state,
                    snackbarHostState = SnackbarHostState(),
                    onRefresh = {},
                    onRetry = onRetry,
                    onPostClick = onPostClick,
                    onDelete = {},
                )
            }
        }
    }

    @Test
    fun postsAreRenderedAndClickable() {
        var clickedId: Int? = null
        setContent(
            state =
                PostListUiState(
                    posts =
                        persistentListOf(
                            Post(id = 1, title = "First title", body = "First body"),
                            Post(id = 2, title = "Second title", body = "Second body"),
                        ),
                ),
            onPostClick = { clickedId = it },
        )

        composeRule.onNodeWithText("First title").assertIsDisplayed()
        composeRule.onNodeWithText("Second body").assertIsDisplayed()

        composeRule.onNodeWithText("Second title").performClick()
        assertEquals(2, clickedId)
    }

    @Test
    fun emptyStateIsShown() {
        setContent(state = PostListUiState())

        composeRule.onNodeWithText("No posts").assertIsDisplayed()
    }

    @Test
    fun errorStateShowsMessageAndRetry() {
        var retried = false
        setContent(
            state =
                PostListUiState(
                    error = ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET),
                ),
            onRetry = { retried = true },
        )

        composeRule.onNodeWithText("No internet connection.").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").performClick()
        assertTrue(retried)
    }
}
