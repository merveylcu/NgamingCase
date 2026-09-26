package com.merveylcu.ngamingcase.feature.posts.domain.repository

import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import kotlinx.coroutines.flow.Flow

public interface PostRepository {
    public fun observePosts(): Flow<List<Post>>
    public fun observePost(id: Int): Flow<Post?>
    public suspend fun isEmpty(): Boolean
    public suspend fun refresh(): RestResult<Unit>
    public suspend fun softDelete(id: Int)
    public suspend fun restore(id: Int)

    /** Sends the delete to the API. Restores the post if the call fails. */
    public suspend fun confirmDelete(id: Int): RestResult<Unit>

    /** Sends the update to the API first. The local copy is written only on success. */
    public suspend fun updatePost(
        id: Int,
        title: String,
        body: String,
    ): RestResult<Unit>
}
