package com.merveylcu.ngamingcase.feature.posts.presentation.postdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
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
        val state = viewModel(FakePostRepository(listOf(post))).uiState.value.content()

        assertThat(state.title).isEqualTo("title")
        assertThat(state.isDirty).isFalse()
        assertThat(state.canSave).isFalse()
    }

    @Test
    fun `blank title cannot be saved`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))

        viewModel.onTitleChange("   ")

        val state = viewModel.uiState.value.content()
        assertThat(state.isTitleValid).isFalse()
        assertThat(state.canSave).isFalse()
    }

    @Test
    fun `edited text is kept in the saved state handle`() = runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel(FakePostRepository(listOf(post)), savedStateHandle).onTitleChange("new title")

        val state = viewModel(
            FakePostRepository(listOf(post)),
            savedStateHandle,
        ).uiState.value.content()

        assertThat(state.title).isEqualTo("new title")
        assertThat(state.canSave).isTrue()
    }

    @Test
    fun `successful save updates the post and navigates back`() = runTest {
        val repository = FakePostRepository(listOf(post))
        val viewModel = viewModel(repository)
        viewModel.onTitleChange("new title")

        viewModel.uiEffect.test {
            viewModel.onSave()
            assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
        }
        assertThat(repository.posts.single().title).isEqualTo("new title")
    }

    @Test
    fun `failed save shows an error and keeps the edited text`() = runTest {
        val repository = FakePostRepository(listOf(post)).apply {
            updateResult = RestResult.Error(networkError)
        }
        val viewModel = viewModel(repository)
        viewModel.onTitleChange("new title")
        viewModel.onBodyChange("new body")

        viewModel.uiEffect.test {
            viewModel.onSave()
            assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.ShowError(networkError))
        }

        val state = viewModel.uiState.value.content()
        assertThat(state.title).isEqualTo("new title")
        assertThat(state.body).isEqualTo("new body")
        assertThat(state.isSaving).isFalse()
        assertThat(state.canSave).isTrue()
        assertThat(repository.posts.single()).isEqualTo(post)
    }

    @Test
    fun `back with unsaved changes shows the discard dialog and confirm navigates back`() =
        runTest {
            val viewModel = viewModel(FakePostRepository(listOf(post)))
            viewModel.onTitleChange("new title")

            viewModel.onBack()
            val dialog = viewModel.uiState.value.dialog()
            assertThat(dialog).isNotNull()

            viewModel.uiEffect.test {
                dialog!!.onConfirm()
                assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
            }
            assertThat(viewModel.uiState.value.dialog()).isNull()
        }

    @Test
    fun `dismissing the discard dialog keeps the edit`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))
        viewModel.onTitleChange("new title")
        viewModel.onBack()

        viewModel.uiEffect.test {
            viewModel.uiState.value.dialog()!!.onDismiss()
            expectNoEvents()
        }
        assertThat(viewModel.uiState.value.dialog()).isNull()
        assertThat(viewModel.uiState.value.content().title).isEqualTo("new title")
    }

    @Test
    fun `back without changes navigates back directly`() = runTest {
        val viewModel = viewModel(FakePostRepository(listOf(post)))

        viewModel.uiEffect.test {
            viewModel.onBack()
            assertThat(awaitItem()).isEqualTo(PostDetailUiEffect.NavigateBack)
        }
    }

    @Test
    fun `missing post shows not found`() = runTest {
        val state = viewModel(FakePostRepository(listOf(post)), postId = 99).uiState.value.content()

        assertThat(state.notFound).isTrue()
    }

    private fun BaseUiState<PostDetailUiState>.content(): PostDetailUiState =
        (this as BaseUiState.Content).data

    private fun BaseUiState<PostDetailUiState>.dialog() = (this as BaseUiState.Content).dialogState
}
