package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.testing.MainDispatcherRule
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.ObservePostUseCase
import com.merveylcu.ngamingcase.feature.posts.domain.usecase.UpdatePostUseCase
import com.merveylcu.ngamingcase.feature.posts.presentation.FakePostRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PostDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val post = Post(id = 1, title = "title", body = "body", imageUrl = "")
    private val networkError = ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)

    private fun viewModel(
        repository: FakePostRepository,
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
        postId: Int = post.id,
    ) = PostDetailViewModel(
        postId = postId,
        savedStateHandle = savedStateHandle,
        observePost = ObservePostUseCase(repository),
        updatePost = UpdatePostUseCase(repository),
    )

    @Test
    fun `unchanged post cannot be saved`() = runTest {
        viewModel(FakePostRepository(listOf(post))).uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.title).isEqualTo("title")
            assertThat(state.isDirty).isFalse()
            assertThat(state.canSave).isFalse()
        }
    }

    @Test
    fun `blank title cannot be saved`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))

        viewModel.uiState.test {
            viewModel.onTitleChange("   ")
            val state = expectMostRecentItem()
            assertThat(state.isTitleValid).isFalse()
            assertThat(state.canSave).isFalse()
        }
    }

    @Test
    fun `edited text is kept in the saved state handle`() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = viewModel(FakePostRepository(listOf(post)), savedStateHandle)

        viewModel.onTitleChange("new title")

        viewModel(FakePostRepository(listOf(post)), savedStateHandle).uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.title).isEqualTo("new title")
            assertThat(state.canSave).isTrue()
        }
    }

    @Test
    fun `successful save updates the post and navigates back`() = runTest {
        val repository = FakePostRepository(listOf(post))
        val viewModel = viewModel(repository)

        viewModel.uiState.test {
            viewModel.onTitleChange("new title")
            assertThat(expectMostRecentItem().canSave).isTrue()

            viewModel.uiEffect.test {
                viewModel.onSave()
                assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
            }
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(repository.posts.single().title).isEqualTo("new title")
    }

    @Test
    fun `failed save shows an error and keeps the edited text`() = runTest {
        val repository =
            FakePostRepository(listOf(post)).apply {
                updateResult =
                    RestResult.Error(networkError)
            }
        val viewModel = viewModel(repository)

        viewModel.uiState.test {
            viewModel.onTitleChange("new title")
            viewModel.onBodyChange("new body")
            expectMostRecentItem()

            viewModel.uiEffect.test {
                viewModel.onSave()
                assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.ShowError(networkError))
            }

            val state = expectMostRecentItem()
            assertThat(state.title).isEqualTo("new title")
            assertThat(state.body).isEqualTo("new body")
            assertThat(state.isSaving).isFalse()
            assertThat(state.canSave).isTrue()
        }
        assertThat(repository.posts.single()).isEqualTo(post)
    }

    @Test
    fun `back with unsaved changes asks for confirmation`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))

        viewModel.uiState.test {
            viewModel.onTitleChange("new title")
            viewModel.onBack()
            assertThat(expectMostRecentItem().isDiscardDialogVisible).isTrue()

            viewModel.uiEffect.test {
                viewModel.onDiscardConfirm()
                assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
            }
            assertThat(expectMostRecentItem().isDiscardDialogVisible).isFalse()
        }
    }

    @Test
    fun `back without changes navigates back directly`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))

        viewModel.uiState.test {
            expectMostRecentItem()
            viewModel.uiEffect.test {
                viewModel.onBack()
                assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `missing post shows not found`() = runTest {
        viewModel(FakePostRepository(listOf(post)), postId = 99).uiState.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.notFound).isTrue()
        }
    }
}
