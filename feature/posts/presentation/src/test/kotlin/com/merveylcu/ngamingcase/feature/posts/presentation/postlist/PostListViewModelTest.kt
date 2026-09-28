package com.merveylcu.ngamingcase.feature.posts.presentation.postlist

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.testing.MainDispatcherRule
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ConfirmDeletePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.IsPostCacheEmptyUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RefreshPostsUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.RestorePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.SoftDeletePostUseCase
import com.merveylcu.ngamingcase.feature.posts.presentation.FakePostRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PostListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val networkError = ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)

    private fun viewModel(repository: FakePostRepository) = PostListViewModel(
        observePosts = ObservePostsUseCase(repository),
        isPostCacheEmpty = IsPostCacheEmptyUseCase(repository),
        refreshPosts = RefreshPostsUseCase(repository),
        softDeletePost = SoftDeletePostUseCase(repository),
        restorePost = RestorePostUseCase(repository),
        confirmDeletePost = ConfirmDeletePostUseCase(repository),
    )

    @Test
    fun `empty cache and successful fetch shows the posts`() = runTest {
        val repository = FakePostRepository().apply { remotePosts = posts(3) }

        viewModel(repository).uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.posts.map { it.id }).containsExactly(1, 2, 3).inOrder()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `empty cache and failed fetch shows the error state`() = runTest {
        val repository =
            FakePostRepository().apply {
                refreshResult =
                    RestResult.Error(
                        networkError,
                    )
            }

        viewModel(repository).uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.posts).isEmpty()
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isEqualTo(networkError)
        }
    }

    @Test
    fun `retry after an error loads the posts`() = runTest {
        val repository =
            FakePostRepository().apply {
                refreshResult =
                    RestResult.Error(
                        networkError,
                    )
            }
        val viewModel = viewModel(repository)

        viewModel.uiState.test {
            assertThat(expectMostRecentItem().error).isEqualTo(networkError)

            repository.refreshResult = RestResult.Success(Unit)
            repository.remotePosts = posts(2)
            viewModel.onRetry()

            val state = expectMostRecentItem()
            assertThat(state.error).isNull()
            assertThat(state.posts).hasSize(2)
        }
    }

    @Test
    fun `refresh failure with content keeps the posts and emits an error effect`() = runTest {
        val repository = FakePostRepository(initialPosts = posts(2))
        val viewModel = viewModel(repository)
        repository.refreshResult = RestResult.Error(networkError)

        viewModel.uiEffect.test {
            viewModel.onRefresh()
            assertThat(awaitItem()).isEqualTo(PostListUiEffect.ShowError(networkError))
        }
        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.posts).hasSize(2)
            assertThat(state.error).isNull()
            assertThat(state.isRefreshing).isFalse()
        }
    }

    @Test
    fun `delete hides the post and emits the undo effect, undo restores it`() = runTest {
        val repository = FakePostRepository(initialPosts = posts(3))
        val viewModel = viewModel(repository)

        viewModel.uiEffect.test {
            viewModel.onDelete(2)
            assertThat(awaitItem()).isEqualTo(PostListUiEffect.ShowUndoDelete(postId = 2))
        }
        assertThat(repository.posts.map { it.id }).containsExactly(1, 3)

        viewModel.onUndoDelete(2)

        assertThat(repository.restoredIds).containsExactly(2)
        assertThat(repository.posts.map { it.id }).containsExactly(1, 2, 3).inOrder()
    }

    @Test
    fun `failed delete confirmation restores the post and emits an error effect`() = runTest {
        val repository =
            FakePostRepository(initialPosts = posts(2)).apply {
                confirmDeleteResult = RestResult.Error(networkError)
            }
        val viewModel = viewModel(repository)

        viewModel.uiEffect.test {
            viewModel.onDelete(1)
            assertThat(awaitItem()).isEqualTo(PostListUiEffect.ShowUndoDelete(postId = 1))

            viewModel.onDeleteConfirm(1)
            assertThat(awaitItem()).isEqualTo(PostListUiEffect.ShowError(networkError))
        }
        assertThat(repository.confirmedDeleteIds).containsExactly(1)
        assertThat(repository.posts.map { it.id }).containsExactly(1, 2).inOrder()
    }

    private fun posts(count: Int) = List(count) { index ->
        Post(id = index + 1, title = "title ${index + 1}", body = "body")
    }
}
