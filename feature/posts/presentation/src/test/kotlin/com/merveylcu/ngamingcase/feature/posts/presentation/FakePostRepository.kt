package com.merveylcu.ngamingcase.feature.posts.presentation

import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [PostRepository] whose network results can be set per test.
 */
class FakePostRepository(initialPosts: List<Post> = emptyList()) : PostRepository {

    private val visible = MutableStateFlow(initialPosts)
    private val deleted = mutableMapOf<Int, Post>()

    var remotePosts: List<Post> = emptyList()
    var refreshResult: RestResult<Unit> = RestResult.Success(Unit)
    var confirmDeleteResult: RestResult<Unit> = RestResult.Success(Unit)
    var updateResult: RestResult<Unit> = RestResult.Success(Unit)

    val restoredIds = mutableListOf<Int>()
    val confirmedDeleteIds = mutableListOf<Int>()
    val posts: List<Post> get() = visible.value

    override fun observePosts(): Flow<List<Post>> = visible

    override fun observePost(id: Int): Flow<Post?> = visible.map { posts -> posts.find { it.id == id } }

    override suspend fun isEmpty(): Boolean = visible.value.isEmpty() && deleted.isEmpty()

    override suspend fun refresh(): RestResult<Unit> {
        if (refreshResult is RestResult.Success) {
            // Upsert like Room: remote posts overwrite by id, deleted ones stay hidden.
            val merged = visible.value.associateBy { it.id } + remotePosts.associateBy { it.id }
            visible.value = merged.values.filterNot { it.id in deleted }.sortedBy { it.id }
        }
        return refreshResult
    }

    override suspend fun softDelete(id: Int) {
        val post = visible.value.find { it.id == id } ?: return
        deleted[id] = post
        visible.value = visible.value - post
    }

    override suspend fun restore(id: Int) {
        restoredIds += id
        val post = deleted.remove(id) ?: return
        visible.value = (visible.value + post).sortedBy { it.id }
    }

    override suspend fun confirmDelete(id: Int): RestResult<Unit> {
        confirmedDeleteIds += id
        if (confirmDeleteResult is RestResult.Error) restore(id)
        return confirmDeleteResult
    }

    override suspend fun updatePost(
        id: Int,
        title: String,
        body: String,
    ): RestResult<Unit> {
        if (updateResult is RestResult.Success) {
            visible.value = visible.value.map { if (it.id == id) it.copy(title = title, body = body) else it }
        }
        return updateResult
    }
}
